package me.acorneo.jobschedulerapi.workers;

import lombok.Data;

@Data
public class FailPayload {
    private float probability;
}
