package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "SubSystemsTeleOP26", group = "LinearOpMode")
public class TeleOP26 extends LinearOpMode {

    private boolean catapultRunning = false;
    private boolean lastCatapultButton = false;

    private static final int CATAPULT_TICKS = 28;
    private static final double CATAPULT_POWER = 0.15;
    public SubSystem26 subsystem26 =  new SubSystem26();
    private double motorSpeed = 1;

    // Intake variables
    private double intakePower = 0.0;
    private double savedIntakePower = 0.0;

    public void runOpMode() {
        //----------------------------------------
        //TELEOP
        //----------------------------------------

        // Recall from Subsystems
        subsystem26.init(hardwareMap);

        telemetry.addLine("Ready!");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            driveMecanum();
            adjustDriveSpeed();
            adjustIntake();
            adjustCarousel();
            runAutomaticCatapult();

            telemetry.addData("Drive Speed", motorSpeed);

            telemetry.addLine("---- Intake ----");
            telemetry.addData("Intake Power", intakePower);
            telemetry.addData("Saved Intake", savedIntakePower);

            telemetry.addLine("---- Catapult ----");
            telemetry.update();
        }
    }
    private void driveMecanum() {
        double drive = gamepad1.left_stick_y;
        double strafe = gamepad1.left_stick_x;
        double rotate = gamepad1.right_stick_x;
        subsystem26.drivetrain.driveMecanum(motorSpeed,drive,strafe,rotate);
    }

    // Drive speed control
    private void adjustDriveSpeed() {
        if (gamepad1.x) motorSpeed = 0.3;
        else if (gamepad1.y) motorSpeed = 0.6;
        else if (gamepad1.b) motorSpeed = 0.9;
        else if (gamepad1.a) motorSpeed = 1.0;
    }

    //----------------------------------------
    //INTAKE
    //----------------------------------------
    // Intake control
    private void adjustIntake() {

        double live = -gamepad2.left_stick_y;
        subsystem26.intake.adjustIntake(intakePower);

        // Live control when stick is moved
        if (Math.abs(live) > 0.05) {
            intakePower = live;
        }

        // Save intake power with left trigger
        if (gamepad2.left_trigger > 0.5) {
            savedIntakePower = intakePower;
        }

        // When stick released, use saved power
        if (Math.abs(live) <= 0.05) {
            intakePower = savedIntakePower;
        }
    }

    //----------------------------------------
    //SHOOTER
    //----------------------------------------
    // Shooter control (67% manual response)

    private void adjustCarousel() {
        subsystem26.carouselMotor.setPower(
                gamepad2.left_bumper ? 0.5 : 0.0);
    }

    private void runAutomaticCatapult() {
        boolean button = gamepad2.right_bumper;

        if (button && !lastCatapultButton && !catapultRunning) {
            int current =
                    subsystem26.catapultMotor.getCurrentPosition();

            subsystem26.catapultMotor.setTargetPosition(
                    current + CATAPULT_TICKS);

            subsystem26.catapultMotor.setMode(
                    DcMotor.RunMode.RUN_TO_POSITION);

            subsystem26.catapultMotor.setPower(CATAPULT_POWER);
            catapultRunning = true;
        }

        lastCatapultButton = button;

        if (catapultRunning &&
                !subsystem26.catapultMotor.isBusy()) {

            subsystem26.catapultMotor.setPower(0);

            subsystem26.catapultMotor.setMode(
                    DcMotor.RunMode.RUN_USING_ENCODER);

            catapultRunning = false;
        }
    }
}