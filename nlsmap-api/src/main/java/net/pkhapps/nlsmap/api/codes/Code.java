package net.pkhapps.nlsmap.api.codes;

import net.pkhapps.nlsmap.api.types.LocalizedString;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * TODO Document me
 */
public interface Code<T> extends Serializable {

    @NotNull T getCode();

    @NotNull LocalizedString getDescription();
}
