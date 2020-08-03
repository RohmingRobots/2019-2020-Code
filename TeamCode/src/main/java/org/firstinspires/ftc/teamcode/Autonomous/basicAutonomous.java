package org.firstinspires.ftc.teamcode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

//This part is importing information from other programs
import org.firstinspires.ftc.teamcode.SubAssembly.Lift.LiftControl;
import org.firstinspires.ftc.teamcode.SubAssembly.FoundationGrabber.FoundationGrabberControl;
import org.firstinspires.ftc.teamcode.SubAssembly.Grabber.GrabberControl;
import org.firstinspires.ftc.teamcode.SubAssembly.DriveTrain.DriveControl;
import org.firstinspires.ftc.teamcode.Utilities.UserControl;

@Autonomous(name = "Basic Autonomous", group = "Auto")
public class basicAutonomous extends LinearOpMode{
    //This gives the control programs shortened names to refer to them in this program
    DriveControl Drive = new DriveControl();
    GrabberControl Grabber = new GrabberControl();
    FoundationGrabberControl FoundationGrabber = new FoundationGrabberControl();
    LiftControl Lift = new LiftControl();

    //State setup
    private void newState(State newState) {
        mCurrentState = newState;
        Drive.stop();
        Drive.TimeDelay(0.1);
    }

    //This is a list of all of the states
    private enum State {
        Initial,
        MoveToStone,
        GrabStone,
        DeliverStone,
        GrabStone2,
        DeliverStone2,
        ParkFromQuarry,
        Park,
        Stop
    }

    //This sets the default starting state
    private State mCurrentState = State.Initial;

    @Override
    public void runOpMode() throws InterruptedException {
        // declare local variables
        double speed = 0.3;

        // display welcome message
        telemetry.setAutoClear(false);
        telemetry.addLine("Autonomous");
        telemetry.update();

        // create and initialize sub-assemblies
        UserControl User = new UserControl();
        User.init(this);
        Drive.init(this);
        Grabber.init(this);
        FoundationGrabber.init(this);
        Lift.initialize(this);

        // get user input
        boolean waiting;
        boolean red;
        boolean justParking;
        boolean secondStone;
        boolean Skybridge;

        //This asks whether you want to delay start or not and whether you are red or blue
        waiting = User.getYesNo("Wait?");
        red = User.getRedBlue("Alliance Color");
        justParking = User.getPark("Park?");
        secondStone = User.getStone("Deliver a second stone?");
        Skybridge = User.getPos("Bridge or Wall?");

        // wait for PLAY button to be pressed on driver station
        telemetry.addLine(">> Press PLAY to start");
        telemetry.update();
        telemetry.setAutoClear(true);
        waitForStart();

        // begin autonomous actions
        telemetry.setAutoClear(false);
        newState(State.Initial);

        while (opModeIsActive() && mCurrentState != State.Stop) {

            //state switch
            switch (mCurrentState) {
                /*Initializes auto and waits for the time delay*/
                case Initial:
                    telemetry.addLine("Initial");
                    telemetry.update();
                    if (waiting) {
                        telemetry.addLine("wait for 5 seconds");
                        telemetry.update();
                        Drive.TimeDelay(5.0);
                    }
                    //checks to see if running full auto or just parking
                    if (justParking){
                        newState(State.ParkFromQuarry);
                    }
                    else {
                        newState(State.MoveToStone);
                    }
                    break;

                case MoveToStone:
                    telemetry.addLine("move to stone");
                    telemetry.update();
                    Grabber.open();
                    Grabber.Pos1(); //extends grabber slightly so it doesn't get caught on the lift
                    Drive.moveForwardDistance(0.8, 75);
                    newState(State.GrabStone);
                    break;

                case GrabStone:
                    telemetry.addLine("grab stone");
                    telemetry.update();
                    Grabber.close();
                    Drive.TimeDelay(0.5);
                    newState(State.DeliverStone);
                    break;

                case DeliverStone:
                    Drive.moveBackwardDistance(0.8,30);
                    if (red) {
                        Drive.turnRightAngle(0.5, 90);
                    }
                    else {
                        Drive.turnLeftAngle(0.5,90);
                    }
                    Drive.moveForwardDistance(0.8, 95);
                    //Drive.moveForwardDistance(0.8, 50);
                    Grabber.open();
                    if(secondStone){
                        newState(State.GrabStone2);
                    }
                    else {
                        newState(State.Park);
                    }
                    break;

                case GrabStone2:
                    Drive.moveBackwardDistance(0.8, 115);
                    if(red){
                        Drive.turnLeftAngle(0.8, 90);
                    }
                    else{
                        Drive.turnRightAngle(0.8, 90);
                    }
                    Drive.moveForwardDistance(0.7, 25);
                    Grabber.close();
                    newState(State.DeliverStone2);
                    break;

                case DeliverStone2:
                    Drive.moveBackwardDistance(0.8, 20);
                    if(!red){
                        Drive.turnLeftAngle(0.8, 90);
                    }
                    else{
                        Drive.turnRightAngle(0.8, 90);
                    }
                    Drive.moveForwardDistance(0.8, 125);
                    Grabber.open();
                    //Drive.moveForwardDistance(0.8, 25);
                    newState(State.Park);
                    break;

                case Park:
                    if(red && Skybridge){
                        Drive.strafeLeftDistance(0.8, 10);
                        //Drive.driveBackwardUntilColor(0.3);
                        Drive.driveBackwardUntilColor(0.3);
                    }
                    else if (!red && Skybridge){
                        Drive.strafeRightDistance(0.8, 10);
                        //Drive.driveBackwardUntilColor(0.3);
                        Drive.driveBackwardUntilColor(0.3);
                    }
                    else if (Skybridge == false){
                        //Drive.driveBackwardUntilColor(0.3);
                        Drive.driveBackwardUntilColor(0.3);
                    }
                    Grabber.close();
                    newState(State.Stop);
                    break;

                case ParkFromQuarry:
                    telemetry.addLine("Park");
                    telemetry.update();
                    if (Skybridge) {
                        Drive.moveForwardDistance(0.8, 65);
                        if (red) {
                            Drive.turnLeftAngle(0.8, 90);
                        } else {
                            Drive.turnRightAngle(0.8, 90);
                        }
                        Drive.moveForwardDistance(0.8, 70);
                    }
                    else {
                        if (red) {
                            Drive.strafeLeftDistance(0.8, 90);
                        } else {
                            Drive.strafeRightDistance(0.8, 90);
                        }
                    }
                    newState(State.Stop);
                    break;


                case Stop:
                    telemetry.addLine("Stop");
                    telemetry.update();
                    break;
            }
        }

        // ensure proper closure of subassemblies
        Drive.TimeDelay(1.0);
        Lift.finalize();
    }
}