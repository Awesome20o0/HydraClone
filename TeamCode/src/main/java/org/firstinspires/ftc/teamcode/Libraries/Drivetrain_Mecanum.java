package org.firstinspires.ftc.teamcode.Libraries;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Varun on 9/11/2017.
 */

public class Drivetrain_Mecanum{
    public DcMotor motorBL;
    public DcMotor motorFL;
    public DcMotor motorBR;
    public DcMotor motorFR;

    public SensorRR sensor;
    int nullValue;
    LinearOpMode opMode;
    BNO055IMU imu;

    public Drivetrain_Mecanum(LinearOpMode opMode)throws InterruptedException {
        this.opMode = opMode;
        nullValue = 0;
        motorFL = this.opMode.hardwareMap.dcMotor.get("FL");
        motorFR = this.opMode.hardwareMap.dcMotor.get("FR");
        motorBL = this.opMode.hardwareMap.dcMotor.get("BL");
        motorBR = this.opMode.hardwareMap.dcMotor.get("BR");
        this.opMode.telemetry.addData("init", "finished drivetrain init");
        this.opMode.telemetry.update();
        sensor = new SensorRR(opMode);
        this.opMode.telemetry.addData("init", "init finished");
        this.opMode.telemetry.update();
    }

    public void resetEncoders() throws InterruptedException {

        motorBL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        opMode.idle();
        motorBR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        opMode.idle();
        motorFR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        opMode.idle();
        motorFL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        opMode.idle();

        motorBL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        opMode.idle();
        motorBR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        opMode.idle();
        motorFR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        opMode.idle();
        motorFL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        opMode.idle();

    }

    public int getEncoderAvg() {
        return ((Math.abs(motorBR.getCurrentPosition())) +
                Math.abs(motorBL.getCurrentPosition()) +
                (Math.abs(motorFR.getCurrentPosition())) +
                Math.abs(motorFL.getCurrentPosition())) / 4;
    }


    public void movepid(double power, int distance, double floor, double kP, double kI, double kD, int accuracy, double rotation, double direction, double timeout) throws InterruptedException {

        double error;
        double inte = 0;
        double der;
        double previousRunTime;

        resetEncoders();

        double previousError = distance - getEncoderAvg();



        opMode.telemetry.addData("distance left", distance + "");
        opMode.telemetry.addData("current Encoder", getEncoderAvg() + "");
        opMode.telemetry.update();

        opMode.resetStartTime();

        while((getEncoderAvg() < (distance - accuracy)) && opMode.getRuntime() < timeout) {
            error = Math.abs(distance) - Math.abs(getEncoderAvg());
            previousRunTime = opMode.getRuntime();
            power = (power * (error) * kP) + floor;
            inte += (((opMode.getRuntime() - previousRunTime)) * error * kI);
            der = (((error - previousError) / (opMode.getRuntime() - previousRunTime)) * kD);

            power = power + inte - der;

            Range.clip(power, -1, 1);
            move(-power, rotation, direction);

            opMode.telemetry.addData("error", error);
            opMode.telemetry.addData("PID", power);
            opMode.telemetry.addData("integral", inte);
            opMode.telemetry.addData("Encoder", getEncoderAvg());

            opMode.telemetry.update();
//            opMode.telemetry.addData("integral", inte);
            previousError = error;
            opMode.idle();
        }

        opMode.telemetry.update();
        stopMotors();
    }

    public void movepid(double power, int distance, double floor, double kP, double kI, double kD, int accuracy, double rotation, double direction) throws InterruptedException {

        double error;
        double inte = 0;
        double der;
        double previousRunTime;

        resetEncoders();

        double previousError = distance - getEncoderAvg();



        opMode.telemetry.addData("distance left", distance + "");
        opMode.telemetry.addData("current Encoder", getEncoderAvg() + "");
        opMode.telemetry.update();

        opMode.resetStartTime();

        while(getEncoderAvg() < (distance - accuracy)) {
            error = Math.abs(distance) - Math.abs(getEncoderAvg());
            previousRunTime = opMode.getRuntime();
            power = (power * (error) * kP) + floor;
            inte += ((opMode.getRuntime() - previousRunTime) * error * kI);
            der = (error - previousError) / (opMode.getRuntime() - previousRunTime) * kD;

            power = power + inte - der;

            Range.clip(power, -1, 1);
            move(-power, rotation, direction);

            opMode.telemetry.addData("error", error);
            opMode.telemetry.addData("PID", power);
            opMode.telemetry.addData("integral", inte);
            opMode.telemetry.addData("Encoder", getEncoderAvg());

            opMode.telemetry.update();
//            opMode.telemetry.addData("integral", inte);
            previousError = error;
            opMode.idle();
        }

        opMode.telemetry.update();
        stopMotors();
    }

    public void strafepid(double power, int distance, double floor, double kP, double kI, double kD, int accuracy, double direction) throws InterruptedException {

        double error;
        double inte = 0;
        double der;
        double previousRunTime;

        resetEncoders();

        double previousError = distance - getEncoderAvg();



        opMode.telemetry.addData("distance left", distance + "");
        opMode.telemetry.addData("current Encoder", getEncoderAvg() + "");
        opMode.telemetry.update();

        opMode.resetStartTime();

        while(getEncoderAvg() < (distance - accuracy)) {
            error = Math.abs(distance) - Math.abs(getEncoderAvg());
            previousRunTime = opMode.getRuntime();
            power = (power * (error) * kP) + floor;
            inte += ((opMode.getRuntime() - previousRunTime) * error * kI);
            der = (error - previousError) / (opMode.getRuntime() - previousRunTime) * kD;

            power = power + inte + der;

            Range.clip(power, -1, 1);
            strafe(power, power, direction);

            opMode.telemetry.addData("error", error);
            opMode.telemetry.addData("PID", power);
            opMode.telemetry.addData("integral", inte);
            opMode.telemetry.addData("Encoder", getEncoderAvg());

            opMode.telemetry.update();
//            opMode.telemetry.addData("integral", inte);
            previousError = error;
            opMode.idle();
        }

        opMode.telemetry.update();
        stopMotors();
    }

    public void strafepid(double power, int distance, double floor, double kP, double kI, double kD, int accuracy, double direction, double timeout) throws InterruptedException {

        double error;
        double inte = 0;
        double der;
        double previousRunTime;

        double heading = sensor.getGyroYaw();

        resetEncoders();

        double previousError = distance - getEncoderAvg();



        opMode.telemetry.addData("distance left", distance + "");
        opMode.telemetry.addData("current Encoder", getEncoderAvg() + "");
        opMode.telemetry.update();

        opMode.resetStartTime();

        while((getEncoderAvg() < (distance - accuracy)) && (opMode.getRuntime() < timeout)) {
            error = Math.abs(distance) - Math.abs(getEncoderAvg());
            previousRunTime = opMode.getRuntime();
            power = (power * (error) * kP) + floor;
            inte += ((opMode.getRuntime() - previousRunTime) * error * kI);
            der = (error - previousError) / (opMode.getRuntime() - previousRunTime) * kD;

            power = power + inte + der;


            Range.clip(power, -1, 1);

            if (sensor.getGyroYaw() > heading + 2)
                strafe(power, power, direction);
            else if (sensor.getGyroYaw() < heading - 2)
                strafe(power, power, direction);
            else
                strafe(power, power, direction);


            opMode.telemetry.addData("error", error);
            opMode.telemetry.addData("PID", power);
            opMode.telemetry.addData("integral", inte);
            opMode.telemetry.addData("Encoder", getEncoderAvg());

            opMode.telemetry.update();
//            opMode.telemetry.addData("integral", inte);
            previousError = error;
            opMode.idle();
        }

        opMode.telemetry.update();
        stopMotors();
    }

    public void move(double pow, double rotation, double direction) {

        final double FL = pow * Math.cos(direction - Math.PI/4) + rotation;
        final double FR = pow * Math.sin(direction - Math.PI/4) - rotation;
        final double BL = pow * Math.sin(direction - Math.PI/4) + rotation;
        final double BR = pow * Math.cos(direction - Math.PI/4) - rotation;

        motorFL.setPower(-FL);
        motorBL.setPower(-BL);
        motorBR.setPower(BR);
        motorFR.setPower(FR);

//    public void move(double pow, double rotation, double direction) {
//
//        final double FL = pow * Math.sin(direction - Math.PI/4) + rotation;
//        final double FR = pow * Math.cos(direction - Math.PI/4) - rotation;
//        final double BL = pow * Math.sin(direction - Math.PI/4) + rotation;
//        final double BR = pow * Math.cos(direction - Math.PI/4) - rotation;
//
//        motorFL.setPower(FL);
//        motorBL.setPower(BL);
//        motorBR.setPower(BR);
//        motorFR.setPower(FR);
    }

    public void strafe(double diag1, double diag2, double direction){

        double current = sensor.getGyroYaw();

        final double FL = diag1 * Math.sin(direction - Math.PI/4);
        final double FR = diag2 * Math.sin(direction - Math.PI/4);
        final double BL = diag2 * Math.cos(direction - Math.PI/4);
        final double BR = diag1 * Math.cos(direction - Math.PI/4);

        if(sensor.getGyroYaw() < current - 2)
        {
            motorFL.setPower(FL);
            motorBL.setPower(BL * 2);
            motorBR.setPower(BR);
            motorFR.setPower(FR * 2);
        } else if (sensor.getGyroYaw() > current + 2)
        {
            motorFL.setPower(FL * 2);
            motorBL.setPower(BL);
            motorBR.setPower(BR * 2);
            motorFR.setPower(FR);
        } else {
            motorFL.setPower(FL);
            motorBL.setPower(BL);
            motorBR.setPower(BR);
            motorFR.setPower(FR);
        }
    }

    public void move(double pow, double rotation, double direction, int encoder) {

        while (getEncoderAvg() < encoder)
            move(pow, rotation, direction);
    }

    public void startMotors(double ri, double le) throws InterruptedException {
            motorBL.setPower(-le);
            motorFL.setPower(-le);
            motorBR.setPower(ri);
            motorFR.setPower(ri);
        }
    public void stopMotors() throws InterruptedException {
        motorBR.setPower(0);
        motorBL.setPower(0);
        motorFL.setPower(0);
        motorFR.setPower(0);
    }

    public void setNullValue() {
        nullValue = getEncoderAvg();
    }

    public void pid(double power, int angleTo, double floor, double kP, double kI, double kD, int accuracy, double timeout) throws InterruptedException {

        double error;
        double inte = 0;
        double der;

        setNullValue();

        double currentAngle = sensor.getGyroYaw();
        double previousError;
        double startTime;

        opMode.telemetry.addData("Current Angle", currentAngle + "");
        opMode.telemetry.addData("Angle To", angleTo + "");
        opMode.telemetry.update();

        opMode.resetStartTime();

        if(currentAngle < angleTo) {

            previousError = angleTo - currentAngle;

            while(currentAngle < angleTo - accuracy && (opMode.getRuntime() < timeout)) {
                startTime = opMode.getRuntime();
                currentAngle = sensor.getGyroYaw();
                error = Math.abs(angleTo) - Math.abs(currentAngle);

                power = ( power * (error) * kP) + floor;
                inte += (((opMode.getRuntime() - startTime)) * error * kI);
                der = (((error - previousError) / (opMode.getRuntime() - startTime)));

                power = power + inte - der;

                Range.clip(power, -1, 1);
                power = Math.abs(power);
                startMotors(-power, power);

                opMode.telemetry.addData("error", error);
                opMode.telemetry.addData("PID", power);
                opMode.telemetry.addData("integral", inte);
                opMode.telemetry.addData("angle", currentAngle);

                opMode.telemetry.update();
                previousError = error;
                opMode.idle();
            }
            opMode.telemetry.update();
            stopMotors();
        }
        else if(currentAngle > angleTo){

            previousError = currentAngle - angleTo;

            while(currentAngle > angleTo + accuracy) {
                startTime = opMode.getRuntime();
                currentAngle = sensor.getGyroYaw();
                error = Math.abs(angleTo) - Math.abs(currentAngle);

                power = ( power * (error) * kP) + floor;
                inte += (((opMode.getRuntime() - startTime)) * error * kI);
                der = (((error - previousError) / (opMode.getRuntime() - startTime)) * kD);

                power = power + inte - der;

                Range.clip(power, -1, 1);
                power = Math.abs(power);
                startMotors(power, -power);

                opMode.telemetry.addData("error", error);
                opMode.telemetry.addData("PID", power);
//            opMode.telemetry.addData("integral", inte);
                opMode.telemetry.addData("integral", inte);
                opMode.telemetry.addData("angle", currentAngle);

                opMode.telemetry.update();
                previousError = error;
                opMode.idle();
            }
            opMode.telemetry.update();
            stopMotors();
        }


    }

}

