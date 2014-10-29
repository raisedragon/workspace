import org.compiere.Adempiere;
import org.compiere.util.Splash;


public class StartupMain
{
	
	
	/**
	 *  Main Method
	 *
	 *  @param args optional start class
	 */
	public static void main (String[] args)
	{
		System.setProperty("org.compiere.report.path","D:/work/OWNS_branches/1.2.12.0.OWMS_0901/java/erp/install/Adempiere/reports/");
		System.setProperty("PropertyFile","D:/software/eclipse-jee-juno-SR1-win32-x86_64/workspace/httpclient/Adempiere.properties");
		
		Splash.getSplash();
		Adempiere.startup(true);     //  error exit and initUI

		//  Start with class as argument - or if nothing provided with Client
		String className = "org.compiere.apps.AMenu";
		for (int i = 0; i < args.length; i++)
		{
			if (!args[i].equals("-debug"))  //  ignore -debug
			{
				className = args[i];
				break;
			}
		}
		//
		try
		{
			Class<?> startClass = Class.forName(className);
			startClass.newInstance();
		}
		catch (Exception e)
		{
			System.err.println("ADempiere starting: " + className + " - " + e.toString());
			e.printStackTrace();
		}
	}   //  main
}
