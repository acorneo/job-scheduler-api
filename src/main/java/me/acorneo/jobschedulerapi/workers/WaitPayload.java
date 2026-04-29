package me.acorneo.jobschedulerapi.workers;

import lombok.Data;

@Data
public class WaitPayload {
    private float duration;
}
