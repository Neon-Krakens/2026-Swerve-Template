package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

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
  // SparkMax shooterIntake = new SparkMax(14, MotorType.kBrushless);
  // SparkMax shooterLeft = new SparkMax(13, MotorType.kBrushless);
  // SparkMax shooterRight = new SparkMax(12, MotorType.kBrushless);
  // SparkMax agitator = new SparkMax(15, MotorType.kBrushless);
  // SparkMax lifter = new SparkMax(9, MotorType.kBrushless);
  // SparkMax turret = new SparkMax(11, MotorType.kBrushless);
  // SparkMax intake = new SparkMax(10, MotorType.kBrushless);

  /**
   * Creates a new Robot and initializes the RobotContainer.
   */
  public Robot() {
    robotContainer = new RobotContainer();
    // http://roborio-3822-frc.local:1181/
    if(Robot.isSimulation()) return;
    // UsbCamera topCamera = CameraServer.startAutomaticCapture();
    // topCamera.setResolution(320/Constants.CAMERA_QUALITY_FACTOR, 240/Constants.CAMERA_QUALITY_FACTOR);
    // topCamera.setFPS(10);
    // intake.initialize();
    // shooter.initialize();
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
    
    //robot pos
    double r = robotContainer.swerveDrive.getPose().getRotation().getRadians();
    double xp = robotContainer.swerveDrive.getPose().getX()+0.19685*Math.cos(r); //TODO: check if this rotates correctly
    double yp = robotContainer.swerveDrive.getPose().getY()+0.19685*Math.sin(r);
    double xv = robotContainer.swerveDrive.getRobotVelocity().vxMetersPerSecond; //TODO: is affected by angular velocity
    double yv = robotContainer.swerveDrive.getRobotVelocity().vxMetersPerSecond;

    System.out.println("Pose: xp: " + xp + ", yp: " + yp + ", xv:" + xv + ", yv: " + yv);

    // position on field, 4.62534 meters X, 4.03479 meters Y
    final double target_x = 4.62534;
    final double target_y = 4.03479;

    //global shooting velocities
    final double target_height = 1.8288;
    final double edge_height = target_height + .3;
    double target_distance = Math.sqrt((target_x-xp)*(target_x-xp)+(target_y-yp)*(target_y-yp));
    double edge_distance = target_distance-.6;
    double x_velocity = Math.sqrt(4.9*target_distance*edge_distance*(target_distance - edge_distance)/(edge_height*target_distance - edge_distance*target_height));
    double z_velocity = x_velocity*target_height/target_distance + 4.9*target_distance/x_velocity;
    
    //local shooting velocities
    double x_target_dir = (target_x - xp)/target_distance;
    double y_target_dir = (target_y - yp)/target_distance;
    double x_rel_velocity = x_target_dir * x_velocity - xv;
    double y_rel_velocity = y_target_dir * x_velocity - yv;
    double shooting_vel_xy = Math.sqrt(x_rel_velocity*x_rel_velocity + y_rel_velocity*y_rel_velocity);
    double shooting_dir_horiz = Math.atan2(y_rel_velocity, x_rel_velocity);
    double shooting_dir_virt = Math.atan2(z_velocity, shooting_vel_xy);
    double shooting_speed = Math.sqrt(shooting_vel_xy*shooting_vel_xy + z_velocity*z_velocity);
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