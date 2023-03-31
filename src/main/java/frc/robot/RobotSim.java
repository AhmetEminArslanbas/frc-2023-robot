package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.simulation.DifferentialDrivetrainSim;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import edu.wpi.first.wpilibj.simulation.SingleJointedArmSim;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.Mechanism2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismLigament2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.util.Color8Bit;

/**
 * Physics simulation of the 2023 robot so the real Robot.java can run on a PC.
 *
 * <p>Drivetrain: kitbot-style differential drive. The right side is mirrored on the real robot
 * (the code never inverts it), so its output is negated here. Arm: one pivot (height
 * adjustment) under gravity; "kol" lifts it and "kolgeri" pulls it back, so the joint sees
 * kol - kolgeri. Gripper ("el"): a separate motor that opens with positive voltage and closes
 * with negative voltage.
 *
 * <p>Set the environment variable FRC_AUTO_DEMO=1 to start in autonomous automatically.
 */
public class RobotSim {
  private final PWMSparkMax left;
  private final PWMSparkMax right;
  private final PWMSparkMax arm;
  private final PWMSparkMax retract;
  private final PWMSparkMax gripper;

  private final DifferentialDrivetrainSim drive =
      DifferentialDrivetrainSim.createKitbotSim(
          DifferentialDrivetrainSim.KitbotMotor.kDualCIMPerSide,
          DifferentialDrivetrainSim.KitbotGearing.k10p71,
          DifferentialDrivetrainSim.KitbotWheelSize.kSixInch,
          null);

  private final SingleJointedArmSim armSim =
      new SingleJointedArmSim(
          DCMotor.getNEO(1), 100.0, SingleJointedArmSim.estimateMOI(0.9, 4.0), 0.9,
          Units.degreesToRadians(-75), Units.degreesToRadians(110), true, Units.degreesToRadians(-75));

  private static final double GRID_FACE_X = 1.85;  // blue grid front + half robot length [m]

  private final Field2d field = new Field2d();
  private final MechanismLigament2d armLigament;
  private final MechanismLigament2d gripperLigament;
  private double gripperOpen = 0.0;

  public RobotSim(PWMSparkMax left, PWMSparkMax right, PWMSparkMax arm, PWMSparkMax retract,
                  PWMSparkMax gripper) {
    this.left = left;
    this.right = right;
    this.arm = arm;
    this.retract = retract;
    this.gripper = gripper;

    // Start in the blue community, facing the grid (the auto drives into it to score).
    drive.setPose(new Pose2d(3.2, 4.42, Rotation2d.k180deg));
    SmartDashboard.putData("Field", field);

    Mechanism2d mech = new Mechanism2d(2.4, 2.0, new Color8Bit(Color.kBlack));
    var pivot = mech.getRoot("pivot", 0.8, 0.6);
    pivot.append(new MechanismLigament2d("tower", 0.6, -90, 10, new Color8Bit(Color.kGray)));
    armLigament = pivot.append(new MechanismLigament2d("arm", 0.9, -75, 8, new Color8Bit(Color.kOrange)));
    gripperLigament = armLigament.append(
        new MechanismLigament2d("gripper", 0.2, 0, 6, new Color8Bit(Color.kYellow)));
    SmartDashboard.putData("Arm", mech);

    demo = "1".equals(System.getenv("FRC_AUTO_DEMO"));
  }

  private final boolean demo;
  private double simTime = 0.0;
  private boolean demoStarted = false;

  // Demo mode: wait a few seconds so the GUI is up, then run autonomous like a match start.
  private void startDemoIfDue() {
    if (!demo || demoStarted || simTime < 4.0) {
      return;
    }
    DriverStationSim.setAllianceStationId(edu.wpi.first.hal.AllianceStationID.Blue1);
    DriverStationSim.setAutonomous(true);
    DriverStationSim.setEnabled(true);
    DriverStationSim.notifyNewData();
    demoStarted = true;
  }

  public void update() {
    simTime += 0.02;
    startDemoIfDue();
    double battery = RobotController.getBatteryVoltage();
    boolean enabled = DriverStation.isEnabled();
    double l = enabled ? left.get() * battery : 0;
    double r = enabled ? -right.get() * battery : 0;
    drive.setInputs(l, r);
    drive.update(0.02);
    // The sim has no collisions: treat the blue grid face as a wall the robot pushes against.
    Pose2d pose = drive.getPose();
    if (pose.getX() < GRID_FACE_X) {
      drive.setPose(new Pose2d(GRID_FACE_X, pose.getY(), pose.getRotation()));
    }
    field.setRobotPose(drive.getPose());

    double armVolts = enabled ? (arm.get() - retract.get()) * battery : 0;
    armSim.setInputVoltage(Math.max(-12, Math.min(12, armVolts)));
    armSim.update(0.02);

    double g = enabled ? gripper.get() : 0;
    gripperOpen = Math.max(0, Math.min(1, gripperOpen + g * 0.05));

    armLigament.setAngle(Units.radiansToDegrees(armSim.getAngleRads()));
    gripperLigament.setAngle(-40 + 80 * gripperOpen);

    SmartDashboard.putNumber("Arm angle (deg)", Units.radiansToDegrees(armSim.getAngleRads()));
    SmartDashboard.putNumber("Drive x (m)", drive.getPose().getX());
    SmartDashboard.putBoolean("Gripper open", gripperOpen > 0.5);
  }
}
