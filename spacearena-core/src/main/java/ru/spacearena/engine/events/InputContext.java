package ru.spacearena.engine.events;

import ru.spacearena.engine.Engine;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-04-04
 */
public interface InputContext {

    boolean enableInput(Engine engine, InputType inputType);

}
