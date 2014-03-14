package ru.spacearena.engine.events;

import ru.spacearena.engine.Engine;

/**
* @author Vyacheslav Mayorov
* @since 2014-14-03
*/
public interface EngineEvent {
    void run(Engine engine);
}
