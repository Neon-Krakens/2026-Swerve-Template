import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkClosedLoopController; //needed for reference
import com.revrobotics.spark.SparkMaxConfig;
import com.revrobotics.spark.SparkRelativeEncoder;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.pathplanner.lib.auto.NamedCommands;
import.edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.TimedRobot;

public class Robot extends TimedRobot {
    SparkMax m_motor = new SparkMax(1, MotorType.kBrushless);
    SparkRelativeEncoder m_encoder = m_motor.getEncoder();
    SparkClosedLoopController m_controller = m_motor.getClosedLoopController();
    XboxController m_xbox = new XboxController(0);

    @Override
    public void robotInit(){
        SparkMaxConfig config = new SparkMaxConfig();

        config.encoder
            .positionConversionFactor(360.0)
            .velocityConversionFactor(360.0 / 60.0); //degrees per second

        config
            .idleMode(IdleMode.kBrake)
            .smartCurrentLimit(40);


        config.closedLoop
            .p(0.1)
            .i(0.0)
            .d(0.0)
            .velocityFF(0.0)
            .outputRange(-0.5,0.5);

            m_motor.configAccessor.apply(config); 

        m_encoder.setPosition(90.0);

        NamedCommands.registerCommand("GoToZero",
            Commands.runonce(() -> m_controller.setReference(0.0, SparkMax.ControlType.kPosition)));
        NamedCommands.registerCommand("GoTo90",
            Commands.runOnce(() -> m_controller.setReference(90.0, Sparkmax.ControlType.kPosition)));
    }


    @Override
    public void teleopPeriodic() {
        double position = m_encoder.getPosition(); //rotation
        double velocity = m_encoder.getVelocity(); //RPM
        boolean atTarget = m_controller.isAtSetpoint();
        if (m_xbox.getAButton()) {
            m_controller.setReference(0.0, SparkMax.ControlType.kPosition);
        }
        else if (m_xbox.getBButton()){
            m_controller.setReference(90.0, SparkMax.ControlType.kPosition);
        }
    }
}