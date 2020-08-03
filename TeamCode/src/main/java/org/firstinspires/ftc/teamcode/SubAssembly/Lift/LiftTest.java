package org.firstinspires.ftc.teamcode.SubAssembly.Lift;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Utilities.GamepadWrapper;

/* Sub Assembly Test OpMode
 * This TeleOp OpMode is used to test the functionality of the specific sub assembly
 */
// Assign OpMode type (TeleOp or Autonomous), name, and grouping
@TeleOp(name = "Lift Test", group = "Test")
public class LiftTest extends LinearOpMode {

    // declare class variables
    LiftControl Lift = new LiftControl();
    double time = 0.5;

    @Override
    public void runOpMode() throws InterruptedException {

        // display welcome message
        telemetry.setAutoClear(false);
        telemetry.addLine("Lift Test: ");
        telemetry.update();

        // create extended gamepads (for press and release options)
        GamepadWrapper egamepad1 = new GamepadWrapper(gamepad1);
        GamepadWrapper egamepad2 = new GamepadWrapper(gamepad2);

        // create and initialize sub-assemblies
        Lift.initialize(this);

        // wait for PLAY button to be pressed on driver station
        telemetry.addLine(">> Press PLAY to start");
        telemetry.update();
        telemetry.setAutoClear(true);
        waitForStart();

        //telling the code to run until you press that giant STOP button on RC
        while (opModeIsActive()) {

            // update extended gamepads
            egamepad1.updateEdge();
            egamepad2.updateEdge();

            telemetry.addLine("Press DPAD to move up/down");
            telemetry.addLine("Release A to lift up for time");
            telemetry.addLine("Release B to lift down for time");

            // adjust values
            final double INC = 0.1;
            final double MIN = 0.1;
            final double MAX = 2.0;
            if (egamepad1.left_bumper.pressed)
                time += INC;
            if (egamepad1.left_trigger.pressed)
                time -= INC;
            time = Math.max(MIN, Math.min(time, MAX));
            telemetry.addLine("Time: " + time);

            // check for move input
            if (egamepad1.dpad_up.state) {
                Lift.MoveUp();
            } else if (egamepad1.dpad_down.state) {
                Lift.MoveDown();
            } else if (egamepad1.a.released) {
                Lift.MoveUpTime(time);
            } else if (egamepad1.b.released) {
                Lift.MoveDownTime(time);
            } else {
                Lift.Stop();
            }

            Lift.Telemetry();
            telemetry.update();

            //let the robot have a little rest, sleep is healthy
            sleep(40);
        }

        // ensure proper closure of subassemblies
        Lift.finalize();
    }
}
