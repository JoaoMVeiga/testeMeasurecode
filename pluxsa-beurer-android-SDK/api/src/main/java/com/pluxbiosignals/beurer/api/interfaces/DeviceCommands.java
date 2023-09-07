package com.pluxbiosignals.beurer.api.interfaces;

public interface DeviceCommands<K, T> {

    K getCommand(T[] cmdArguments);
}
