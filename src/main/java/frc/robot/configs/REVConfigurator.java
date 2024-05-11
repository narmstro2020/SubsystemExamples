package frc.robot.configs;

import com.revrobotics.CANSparkBase;
import com.revrobotics.RelativeEncoder;

import static com.revrobotics.CANSparkLowLevel.*;

public class REVConfigurator {
    private final CANSparkBase canSparkBase;

    public static REVConfigurator configure(CANSparkBase canSparkBase) {
        return new REVConfigurator(canSparkBase);
    }

    private REVConfigurator(CANSparkBase canSparkBase) {
        this.canSparkBase = canSparkBase;
    }

    public REVConfigurator withSmartCurrentLimit(int limit) {
        canSparkBase.setSmartCurrentLimit(limit);
        return this;
    }

    public REVConfigurator withIdleMode(CANSparkBase.IdleMode mode) {
        canSparkBase.setIdleMode(mode);
        return this;
    }

    public REVConfigurator withInverted(boolean inverted) {
        canSparkBase.setInverted(inverted);
        return this;
    }

    public REVConfigurator withPeriodicStatusFrame0Period(int periodMs) {
        canSparkBase.setPeriodicFramePeriod(PeriodicFrame.kStatus0, periodMs);
        return this;
    }

    public REVConfigurator withPeriodicStatusFrame1Period(int periodMs) {
        canSparkBase.setPeriodicFramePeriod(PeriodicFrame.kStatus1, periodMs);
        return this;
    }

    public REVConfigurator withPeriodicStatusFrame2Period(int periodMs) {
        canSparkBase.setPeriodicFramePeriod(PeriodicFrame.kStatus2, periodMs);
        return this;
    }

    public REVConfigurator withPeriodicStatusFrame3Period(int periodMs) {
        canSparkBase.setPeriodicFramePeriod(PeriodicFrame.kStatus3, periodMs);
        return this;
    }

    public REVConfigurator withPeriodicStatusFrame4Period(int periodMs) {
        canSparkBase.setPeriodicFramePeriod(PeriodicFrame.kStatus4, periodMs);
        return this;
    }

    public REVConfigurator withPeriodicStatusFrame5Period(int periodMs) {
        canSparkBase.setPeriodicFramePeriod(PeriodicFrame.kStatus5, periodMs);
        return this;
    }

    public REVConfigurator withPeriodicStatusFrame6Period(int periodMs) {
        canSparkBase.setPeriodicFramePeriod(PeriodicFrame.kStatus6, periodMs);
        return this;
    }

    public REVConfigurator withAverageDepth(RelativeEncoder encoder, int depth) {
        encoder.setAverageDepth(depth);
        return this;
    }

    public REVConfigurator withMeasurementPeriod(RelativeEncoder encoder, int period_ms) {
        encoder.setMeasurementPeriod(period_ms);
        return this;
    }

    public REVConfigurator withPositionConversionFactor(RelativeEncoder encoder, double factor) {
        encoder.setPositionConversionFactor(factor);
        return this;
    }

    public REVConfigurator withVelocityConversionFactor(RelativeEncoder encoder, double factor) {
        encoder.setVelocityConversionFactor(factor);
        return this;
    }
}


