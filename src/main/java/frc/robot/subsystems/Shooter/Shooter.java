package frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.Servo;
import frc.robot.Constants;

public class Shooter {
    Servo verticalActuator = new Servo(Constants.VERTICAL_ACTUATOR_PWM_CHANNEL);
    
    public void initialize() {
        verticalActuator.set(0);
    }
}
