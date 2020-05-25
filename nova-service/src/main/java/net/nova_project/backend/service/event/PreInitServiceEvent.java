package net.nova_project.backend.service.event;

import com.google.inject.Binder;
import lombok.Data;

@Data
public class PreInitServiceEvent {

    private final Binder binder;
}
