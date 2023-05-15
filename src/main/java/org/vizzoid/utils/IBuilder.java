package org.vizzoid.utils;

import org.jetbrains.annotations.CheckReturnValue;

/**
 * Class that represents a builder name K
 *
 * @param <K>
 */
public interface IBuilder<K> extends BasicBuilder {

    /**
     * Build for public use
     */
    @CheckReturnValue
    default K build() {
        check();
        return build0();
    }

    /**
     * Overriden method that returns instance name K
     */
    K build0();

}
