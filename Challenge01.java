import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;
import lejos.nxt.TouchSensor;

public class Challenge01
{
	private TouchSensor touchSensor;
	private boolean done;
	public static void main(String[] args)
	{
		final Challenge01 robot;
		robot = new Challenge01();
		robot.run();
	}
	
	public Challenge01()
	{
		final SensorPort touchPort;
		touchPort = SensorPort.S2;
		touchSensor = new TouchSensor(touchPort);
		touchPort.addSensorPortListener(new TouchListener());
	}
	
	public void run()
	{
		// Setting Defaults
		int distance;
		Motor.B.setSpeed(360);
		Motor.C.setSpeed(360);
		
		// First Hall
		moveForwards();
		
		// <LEFT> First Turn
		waitForStop();
		
		// Second Hall
		moveForwards();
		
		// <LEFT> Second Turn
		waitForStop();
		
		// Third Hall
		moveForwards();
		
		// <RIGHT> Third Turn
		waitForStop();
		
		// Fourth Hall
		moveForwards();
		
		// <RIGHT> Fourth Turn
		waitForStop();
		
		// Final Hall
		moveForwards();
	}
	
	private void moveForwards()
	{
		Motor.B.forward();
		Motor.C.forward();
	}
	
	private class TouchListener implements SensorPortListener
	{
		public void stateChanged(final SensorPort port,
		final int oldValue, final int newValue)
		{
			if(touchSensor.isPressed())
			{
				stop();
			}
		}
	}
	
	/** 
	 *  direction 1 Left ||| -1 Right
	 */
	private void turn(int direction)
	{
		Motor.B.rotate(90 * direction, true);
		Motor.C.rotate(-90 * direction, true);
	}
	
	private void waitForStop()
	{
		while(!(done))
		{
			try
			{
				synchronized(this)
				{
					wait(0);
				}
			}
			catch(final InterruptedException ex)
			{
			}
		}
		
		done = false;
	}
	
	private void stop()
	{
		synchronized(this)
		{
			Motor.B.stop();
			Motor.C.stop();
			
			done = true;
			notifyAll();
		}
	}
}