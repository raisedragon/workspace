package com.raise.commons.id.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author RaiseDragon
 * 每一个 ID 都是 64 位数字，由时间戳、节点号和序列编号组成。
 * 序列编号有 12 位，意味着每个节点在每毫秒可以产生 4096 个 ID。
 * 节点号在源码中被分成两部分，数据中心的 ID 和节点 ID，各自占 5 位。
 * 时间戳则是记录了从 1288834974657 (Thu, 04 Nov 2010 01:42:54 GMT) 这一时刻到当前时间所经过的毫秒数，占 41 位（还有一位是符号位，永远为 0）。
 *
 */
public class DefaultIdGeneratorImpl {
	protected static final Logger LOG = LoggerFactory
			.getLogger(DefaultIdGeneratorImpl.class);

	private long workerId;
	private long datacenterId;
	private long sequence = 0L;

	private long twepoch = 1288834974657L;

	private long workerIdBits = 5L;
	private long datacenterIdBits = 5L;
	private long maxWorkerId = -1L ^ (-1L << workerIdBits);
	private long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
	private long sequenceBits = 12L;

	private long workerIdShift = sequenceBits;
	private long datacenterIdShift = sequenceBits + workerIdBits;
	private long timestampLeftShift = sequenceBits + workerIdBits
			+ datacenterIdBits;
	private long sequenceMask = -1L ^ (-1L << sequenceBits);

	private long lastTimestamp = -1L;

	public DefaultIdGeneratorImpl(long workerId, long datacenterId) {
		// sanity check for workerId
		if (workerId > maxWorkerId || workerId < 0) {
			throw new IllegalArgumentException(String.format(
					"worker Id can't be greater than %d or less than 0",
					maxWorkerId));
		}
		if (datacenterId > maxDatacenterId || datacenterId < 0) {
			throw new IllegalArgumentException(String.format(
					"datacenter Id can't be greater than %d or less than 0",
					maxDatacenterId));
		}
		this.workerId = workerId;
		this.datacenterId = datacenterId;
		LOG.info(String
				.format("worker starting. timestamp left shift %d, datacenter id bits %d, worker id bits %d, sequence bits %d, workerid %d",
						timestampLeftShift, datacenterIdBits, workerIdBits,
						sequenceBits, workerId));
	}

	/* (non-Javadoc)
	 * @see com.raise.commons.id.IdGenerator#nextId()
	 */
	public synchronized long nextId() {
		long timestamp = timeGen();

		if (timestamp < lastTimestamp) {
			LOG.error(String.format(
					"clock is moving backwards.  Rejecting requests until %d.",
					lastTimestamp));
			throw new RuntimeException(
					String.format(
							"Clock moved backwards.  Refusing to generate id for %d milliseconds",
							lastTimestamp - timestamp));
		}

		if (lastTimestamp == timestamp) {
			sequence = (sequence + 1) & sequenceMask;
			if (sequence == 0) {
				timestamp = tilNextMillis(lastTimestamp);
			}
		} else {
			sequence = 0L;
		}

		lastTimestamp = timestamp;

		return ((timestamp - twepoch) << timestampLeftShift)
				| (datacenterId << datacenterIdShift)
				| (workerId << workerIdShift) | sequence;
	}

	protected long tilNextMillis(long lastTimestamp) {
		long timestamp = timeGen();
		while (timestamp <= lastTimestamp) {
			timestamp = timeGen();
		}
		return timestamp;
	}

	protected long timeGen() {
		return System.currentTimeMillis();
	}
	
	public static void main(String[] args)
	{
		DefaultIdGeneratorImpl idg = new DefaultIdGeneratorImpl(1, 1);
		Long id = idg.nextId();
		System.out.println(id);
	}
}
