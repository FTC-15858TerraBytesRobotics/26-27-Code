package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class intake26 {

    //Intake motors
    private DcMotor intakeMotor;
    public DcMotor carouselMotor;
    public void init(HardwareMap hardwareMap) {
        // Hardware mapping
        intakeMotor  = hardwareMap.get(DcMotor.class, "intake");
        carouselMotor = hardwareMap.get(DcMotor.class, "carousel");

        // Motor directions
        intakeMotor.setDirection(DcMotor.Direction.FORWARD);
        carouselMotor.setDirection(DcMotor.Direction.FORWARD);
    }
    // Intake control
    public void adjustIntake(double intakePower) {
        intakeMotor.setPower(intakePower);
    }
}

