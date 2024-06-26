package frc.robot.subsystems;


import com.goatlib.periodic.PeriodicTask;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.units.*;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import com.goatlib.mechanisms.flywheels.Flywheel;
import com.goatlib.mechanisms.SimpleMotorConfigs;

import static edu.wpi.first.units.Units.*;

public class FlywheelSubsystem extends SubsystemBase {

    private final Flywheel flywheel;
    private final MutableMeasure<Velocity<Angle>> velocitySetpoint = MutableMeasure.zero(RadiansPerSecond);
    private final SysIdRoutine sysIdRoutine;
    private boolean sysIdActive = false;


    public FlywheelSubsystem(
            Flywheel flywheel,
            SimpleMotorConfigs flywheelConfigs,
            PeriodicTask addPeriodic) {
        this.flywheel = flywheel;

        addPeriodic.accept(flywheel::update, flywheelConfigs.updatePeriodSeconds(), flywheelConfigs.updatePeriodOffsetSeconds());

        addPeriodic.accept(
                () -> {
                    if (!sysIdActive) {
                        double input = flywheel.velocityControlLoop.getOutput(
                                flywheel.velocity.in(RadiansPerSecond),
                                velocitySetpoint.in(RadiansPerSecond));
                        flywheel.setInput(input);
                    }
                },
                flywheelConfigs.controlLoopPeriodSeconds(),
                flywheelConfigs.controlLoopPeriodOffsetSeconds());

        sysIdRoutine = new SysIdRoutine(
                // Empty config defaults to 1 volt/second ramp rate and 7 volt step voltage.
                new SysIdRoutine.Config(),
                new SysIdRoutine.Mechanism(
                        // Tell SysId how to plumb the driving voltage to the motor(s).
                        (voltage) -> flywheel.setInput(voltage.in(Volts)),
                        // Tell SysId how to record a frame of data for each motor on the mechanism being
                        // characterized.
                        log -> {
                            // Record a frame for the shooter motor.
                            log.motor("flywheel")
                                    .voltage(flywheel.voltage)
                                    .angularVelocity(flywheel.velocity);
                        },
                        // Tell SysId to make generated commands require this subsystem, suffix test state in
                        // WPILog with this subsystem's name ("shooter")
                        this,
                        flywheelConfigs.name()));
    }

    public Trigger createAtSetpointTrigger(Measure<Velocity<Angle>> setpoint, Measure<Velocity<Angle>> tolerance) {
        return new Trigger(() -> MathUtil.isNear(setpoint.in(RadiansPerSecond), flywheel.velocity.in(RadiansPerSecond), tolerance.in(RadiansPerSecond)));
    }


    public Command createSetVelocityCommand(Measure<Velocity<Angle>> velocity) {
        return Commands.sequence(
                        runOnce(() -> sysIdActive = false),
                        run(() -> velocitySetpoint.mut_setMagnitude(velocity.in(RadiansPerSecond))))
                .withName(String.format("Velocity set to %s rad/s", velocity.in(RadiansPerSecond)));
    }

    /**
     * Returns a command that will execute a quasistatic test in the forward direction.
     */
    public Command sysIdQuasistaticForward() {
        return Commands.sequence(
                        runOnce(() -> sysIdActive = true),
                        sysIdRoutine.quasistatic(SysIdRoutine.Direction.kForward))
                .withName("sysIdQuasiForward");
    }

    /**
     * Returns a command that will execute a quasistatic test in the reverse direction.
     */
    public Command sysIdQuasistaticReverse() {
        return Commands.sequence(
                        runOnce(() -> sysIdActive = true),
                        sysIdRoutine.quasistatic(SysIdRoutine.Direction.kReverse))
                .withName("sysIdQuasiReverse");
    }

    /**
     * Returns a command that will execute a dynamic test in the forward direction.
     */
    public Command sysIdDynamicForward() {
        return Commands.sequence(
                        runOnce(() -> sysIdActive = true),
                        sysIdRoutine.dynamic(SysIdRoutine.Direction.kForward))
                .withName("sysIdDynamicForward");
    }

    /**
     * Returns a command that will execute a dynamic test in the reverse direction.
     */
    public Command sysIdDynamicReverse() {
        return Commands.sequence(
                        runOnce(() -> sysIdActive = true),
                        sysIdRoutine.dynamic(SysIdRoutine.Direction.kReverse))
                .withName("sysIdDynamicReverse");
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        super.initSendable(builder);
        builder.addDoubleProperty(
                "Velocity (rpm)",
                () -> flywheel.velocity.in(RPM),
                null);
        builder.addDoubleProperty(
                "Velocity Setpoint (rpm)",
                () -> velocitySetpoint.in(RPM),
                null);
        builder.addDoubleProperty(
                "Voltage (Volts)",
                () -> flywheel.voltage.in(Volts),
                null);
        builder.addDoubleProperty(
                "Current (Amps)",
                () -> flywheel.current.in(Amps),
                null);
    }
}


