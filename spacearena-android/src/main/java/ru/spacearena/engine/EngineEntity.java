package ru.spacearena.engine;

import ru.spacearena.engine.handlers.DrawHandler;
import ru.spacearena.engine.handlers.SizeHandler;
import ru.spacearena.engine.handlers.TouchHandler;
import ru.spacearena.engine.handlers.UpdateHandler;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-11-02
 */
public interface EngineEntity extends DrawHandler, SizeHandler, TouchHandler, UpdateHandler {

}
