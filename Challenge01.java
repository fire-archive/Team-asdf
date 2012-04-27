import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;

public class Challenge01
{
	private TouchSensor touchSensor;
	
	private final SensorPort ultrasonicPort;
	private final UltrasonicSensor ultrasonicSensor;
	
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
		
		ultrasonicPort = SensorPort.S1;
		ultrasonicSensor = new UltrasonicSensor(ultrasonicPort);
	}
	
	public void run()
	{
		// Setting Defaults
		int distance;
		Motor.B.setSpeed(720);
		Motor.C.setSpeed(710);
		
		// First Hall
		moveForwards();
		
		// <RIGHT> First Turn
		waitForStop();
		reverse();
		turn(-1);
		
		// Second Hall
		moveForwards();
		
		// <RIGHT> Second Turn
		waitForStop();
		reverse();
		turn(-1);
		
		// Third Hall
		moveForwards();
		
		// <LEFT> Third Turn
		waitForStop();
		reverse();
		turn(1);
		
		// Fourth Hall
		moveForwards();
		
		// <LEFT> Fourth Turn
		waitForStop();
		reverse();
		turn(1);
		
		// Final Hall
		moveForwards();
		
		waitForStop();
	}
	
	public void run2()
	{
		// Setting Defaults
		int distance;
		Motor.B.setSpeed(720);
		Motor.C.setSpeed(710);
		
		// First Hall
		moveForwards();
		
		// <RIGHT> First Turn
		waitForDistance(8);
		turn(1);
		
		// Second Hall
		moveForwards();
		
		// <RIGHT> Second Turn
		waitForDistance(8);
		turn(1);
		
		// Third Hall
		moveForwards();
		
		// <LEFT> Third Turn
		waitForDistance(8);
		turn(-1);
		
		// Fourth Hall
		moveForwards();
		
		// <LEFT> Fourth Turn
		waitForDistance(8);
		turn(-1);
		
		// Final Hall
		moveForwards();
		
		waitForDistance(8);
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
		Motor.B.rotate(190 * direction, true);
		Motor.C.rotate(-190 * direction);
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
	}
	
	private void stop()
	{
		synchronized(this)
		{
			Motor.B.stop();
			Motor.C.stop();
			
			//Motor.B.rotate(-20, true);
			//Motor.C.rotate(-20, true);
			
			done = true;
			notifyAll();
		}
	}
	
	private void reverse()
	{
		if(done == true) 
		{
			done = false;
		}
		synchronized(this)
		{
			Motor.B.rotate(-90, true);
			Motor.C.rotate(-90);	
		}
	}
	
	private int waitForDistance(final int max)
	{
		int distance;
		
		do
		{
			try
			{
				// pause 100 ms between reads...
				// this is a LeJOS issue
				// later versions of LeJOS won't require this
				Thread.sleep(100);
			}
			catch (InterruptedException ex)
			{
			}
			
			distance = ultrasonicSensor.getDistance();
		}
		
		while(distance > max);
		return (distance);
	}
}