package tp1;
import java.io.*;
public class lire {
	public static String S()
	{
		String tmp = "";
		char C = '\0';
		try
		{
			while ((C = (char) System.in.read()) != '\n')
			{
				if (C != '\r')
					tmp = tmp + C;

			}
		}
		catch (IOException e)
		{
			System.out.println("Erreur de frappe");
			System.exit(0);
		}
		return tmp;
	}

	public static byte b()
	{
		byte x = 0;
		try
		{
			x = Byte.parseByte(S());
		}
		catch (NumberFormatException e)
		{
			System.out.println("Format numérique incorrect");
			System.exit(0);
		}
		return x;
	}

	public static short s()
	{
		short x = 0;
		try
		{
			x = Short.parseShort(S());
		}
		catch (NumberFormatException e)
		{
			System.out.println("Format numérique incorrect");
			System.exit(0);
		}
		return x;
	}

	public static int i()
	{
		int x = 0;
		try
		{
			x = Integer.parseInt(S());
		}
		catch (NumberFormatException e)
		{
			System.out.println("Format numérique incorrect");
			System.exit(0);
		}
		return x;
	}

	public static long l()
	{
		long x = 0;
		try
		{
			x = Integer.parseInt(S());
		}
		catch (NumberFormatException e)
		{
			System.out.println("Format numérique incorrect");
			System.exit(0);
		}
		return x;
	}

	public static double d()
	{
		double x = 0.0;
		try
		{
			x = Double.valueOf(S()).doubleValue();
		}
		catch (NumberFormatException e)
		{
			System.out.println("Format numérique incorrect");
			System.exit(0);
		}
		return x;
	}

	public static float f()
	{
		float x = 0.0f;
		try
		{
			x = Double.valueOf(S()).floatValue();
		}
		catch (NumberFormatException e)
		{
			System.out.println("Format numérique incorrect");
			System.exit(0);
		}
		return x;
	}

	public static char c()
	{
		String tmp = S();
		if (tmp.length() == 0)
			return '\n';
		else
		{
			return tmp.charAt(0);
		}
	}
}
