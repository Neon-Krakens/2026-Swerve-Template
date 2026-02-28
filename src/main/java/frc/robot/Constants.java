package frc.robot;

import static edu.wpi.first.units.Units.MetersPerSecond;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.units.measure.LinearVelocity;

public class Constants {
    public static final LinearVelocity MAX_SPEED = MetersPerSecond.of(8.0);
    public static final Double MAX_ANGULAR_VELOCITY = Units.degreesToRadians(720*2);
    public static final double MAX_SPEED2 = Units.feetToMeters(14.5);
    public static final double DRIVER_DEADBAND = 0.07;

    public static final int CAMERA_QUALITY_FACTOR = 4;
    public static final int INTAKE_ROLLER_MOTOR_ID = 16;
    public static final int INTAKE_ROTATION_MOTOR_ID = 15;
    
    public static final int VERTICAL_ACTUATOR_PWM_CHANNEL = 1;
}