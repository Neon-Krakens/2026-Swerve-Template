package frc.robot.subsystems.Bot;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Agitator extends SubsystemBase {
    SparkMax agitator = new SparkMax(15, MotorType.kBrushless);

    public Command funnelForward() {
        return Commands.runOnce(()->{
            System.out.println("Setting funnel speed to "+speed);
            agitator.set(-100);
        });
    }

    public Command funnelReverse() {
        return Commands.runOnce(()->{
            agitator.set(-100.0);
        });
    }

    public Command funnelStop() {
        return Commands.runOnce(()->{
            agitator.set(0.0);
        });
    }
}
