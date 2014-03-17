package ru.spacearena.engine.common;

import ru.spacearena.engine.Engine;
import ru.spacearena.engine.EngineContainer;
import ru.spacearena.engine.EngineEntity;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-16-02
 */
public class GenericContainer extends EngineContainer<EngineEntity> {
    public GenericContainer() {
    }

    public GenericContainer(Engine engine) {
        super(engine);
    }
}
