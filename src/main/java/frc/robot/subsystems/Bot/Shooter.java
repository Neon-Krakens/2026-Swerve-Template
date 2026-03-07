package frc.robot.subsystems.Bot;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Shooter extends SubsystemBase {
    SparkMax shooterIntake = new SparkMax(14, MotorType.kBrushless);
    SparkMax shooterLeft = new SparkMax(13, MotorType.kBrushless);
    SparkMax shooterRight = new SparkMax(12, MotorType.kBrushless);

    public Command spinShooterIntake() {
        return Commands.runOnce(()->{
            shooterIntake.set(-100.0);
        });
    }
    
    public Command shootReverse() {
        return Commands.runOnce(()->{
            shooterLeft.set(100.0);
            shooterRight.set(-100.0);
        });
    }

    public Command shootForward() {
        return Commands.runOnce(()->{
            shooterLeft.set(-100.0);
            shooterRight.set(100.0);
        });
    }

    public Command spinStop() {
        return Commands.runOnce(()->{
            shooterLeft.set(0.0);
            shooterRight.set(0.0);
        });
    }

    public Command goToLaunchAngle(double angle) {
        return Commands.runOnce(()->{});
    }

    public Command turnToAngle(double angle) {
        return Commands.runOnce(()->{});
    }
}
