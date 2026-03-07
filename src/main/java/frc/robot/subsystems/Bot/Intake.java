package frc.robot.subsystems.Bot;

import com.pathplanner.lib.auto.NamedCommands;
import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Intake extends SubsystemBase {
    SparkMax intake = new SparkMax(10, MotorType.kBrushless);

    public Command spinIntakeForward() {
        return Commands.runOnce(()->{
            intake.set(-100.0);
        });
    }

    public Command spinIntakeReverse() {
        return Commands.runOnce(()->{
            intake.set(100.0);
        });
    }

    public Command spinIntakeStop() {
        return Commands.runOnce(()->{
            intake.set(0.0);
        });
    }
}
