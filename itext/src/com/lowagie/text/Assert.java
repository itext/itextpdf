package com.lowagie.text;







public class Assert extends RuntimeException

{

	

	static public void assert(boolean aCondition)

	{

		if (aCondition == false)

		{

			throw new AssertionError();

		}

	}





	static public void assert(boolean aCondition, String message)

	{

		if (aCondition == false)

		{

			throw new AssertionError(message);

		}

	}



	

}

