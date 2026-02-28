package frc.robot;

import com.pathplanner.lib.auto.NamedCommands;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkRelativeEncoder;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.Intake.Intake;
import frc.robot.subsystems.Shooter.Shooter;

/**
 * Main robot class that manages the robot's lifecycle and operational modes.
 * This class follows the TimedRobot model, executing code in a timed loop for
 * each robot mode.
 * 
 * <p>
 * Features include:
 * <ul>
 * <li>Autonomous command management
 * <li>Teleop control initialization
 * <li>Test mode handling
 * <li>Periodic updates across all modes
 * </ul>
 * <p>
 * <p>
 * The robot code is organized using WPILib's command-based framework, with
 * subsystems
 * and commands managed through the RobotContainer class.
 */
public class Robot extends TimedRobot {
  /** The autonomous command to run. */
  private Command autonomousCommand;

  /** The robot's container, which holds subsystems and commands. */
  private final RobotContainer robotContainer;
  
  XboxController controller = new XboxController(0);
  SparkMax rollerMotor = new SparkMax(Constants.INTAKE_ROLLER_MOTOR_ID, MotorType.kBrushless);
  SparkMax rotationMotor = new SparkMax(Constants.INTAKE_ROTATION_MOTOR_ID, MotorType.kBrushless);
  SparkClosedLoopController rotationMotorController = rotationMotor.getClosedLoopController();
  RelativeEncoder rotationMotorEncoder = rotationMotor.getEncoder();
  Intake intake = new Intake();
  Shooter shooter = new Shooter();

  



  /**
   * Creates a new Robot and initializes the RobotContainer.
   */
  public Robot() {
    robotContainer = new RobotContainer();
    // http://roborio-3822-frc.local:1181/
    if(Robot.isSimulation()) return;
    UsbCamera topCamera = CameraServer.startAutomaticCapture();
    topCamera.setResolution(320/Constants.CAMERA_QUALITY_FACTOR, 240/Constants.CAMERA_QUALITY_FACTOR);
    topCamera.setFPS(10);
    intake.initialize();
    shooter.initialize();
  }

  /**
   * Periodic function called every 20ms in all robot modes.
   * Runs the command scheduler to execute ongoing commands and update subsystems.
   * This method is crucial for continuous robot operation and should not be
   * blocked
   * or delayed.
   */
  boolean lastColor = false;

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
    int estimatedPercentage = (int) ((RobotController.getBatteryVoltage() - 11.3) / (12.8 - 11.3) * 100.0);
    SmartDashboard.putString("Est. Battery", estimatedPercentage + "%");

    SmartDashboard.putNumber("Match Time", DriverStation.getMatchTime());
  }

  /** Called once when the robot is disabled. */
  @Override
  public void disabledInit() {
  }

  /** Called periodically when the robot is disabled. */
  @Override
  public void disabledPeriodic() {
  }

  /** Called once when disabled mode is exited. */
  @Override
  public void disabledExit() {
  }

  /**
   * Called when autonomous mode is initialized.
   * Retrieves and schedules the selected autonomous command from RobotContainer.
   * If no command is selected, this method will gracefully handle the null case.
   */
  @Override
  public void autonomousInit() {
    autonomousCommand = robotContainer.getAutonomousCommand();

    if (autonomousCommand != null) {
      autonomousCommand.schedule();
    }
  }

  /** Called periodically during autonomous mode. */
  @Override
  public void autonomousPeriodic() {
  }

  /** Called once when autonomous mode is exited. */
  @Override
  public void autonomousExit() {
  }

  /**
   * Called when operator control is initialized.
   * Cancels any running autonomous command to ensure safe transition to manual
   * control.
   * This prevents autonomous commands from interfering with driver inputs.
   */
  @Override
  public void teleopInit() {
    if (autonomousCommand != null) {
      autonomousCommand.cancel();
    }
  }

  /** Called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    // double position = rotationMotorEncoder.getPosition(); // rotation
    //double velocity = rotationMotorEncoder.getVelocity(); // RPM

    if (controller.getXButton()) {
        // Set the rollerMotor to run at a constant speed, e.g., 20% power
        rollerMotor.set(0.2);
    } else if (controller.getYButton()) {
        // Stop the motor when the button is not pressed
        rollerMotor.set(0);
    }
    if (controller.getAButton()) {
        // Set the rotationMotor to a specific position, e.g., 90 degrees
        rotationMotorController.setReference(90.0, SparkClosedLoopController.ControlType.kPosition);
    } else if (controller.getBButton()) {
        // Stop the motor when the button is not pressed
        rotationMotorController.setReference(0.0, SparkClosedLoopController.ControlType.kPosition);
    }
  
  }

  /** Called once when operator control is exited. */
  @Override
  public void teleopExit() {
  }

  /**
   * Called when test mode is initialized.
   * Cancels all running commands to ensure a clean slate for testing.
   * This helps prevent interference from commands that might have been
   * left running in other modes.
   */
  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  /** Called periodically during test mode. */
  @Override
  public void testPeriodic() {
  }

  /** Called once when test mode is exited. */
  @Override
  public void testExit() {
  }
}