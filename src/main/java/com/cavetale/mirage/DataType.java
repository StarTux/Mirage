package com.cavetale.mirage;

import java.util.Optional;
import java.util.UUID;
import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.DataWatcherRegistry;
import net.minecraft.server.v1_13_R2.DataWatcherSerializer;
import net.minecraft.server.v1_13_R2.EnumDirection;
import net.minecraft.server.v1_13_R2.IBlockData;
import net.minecraft.server.v1_13_R2.IChatBaseComponent;
import net.minecraft.server.v1_13_R2.ItemStack;
import net.minecraft.server.v1_13_R2.NBTTagCompound;
import net.minecraft.server.v1_13_R2.ParticleParam;
import net.minecraft.server.v1_13_R2.Vector3f;

public final class DataType<T> {
    public static final DataType<Byte> BYTE = new DataType<>(DataWatcherRegistry.a);
    public static final DataType<Integer> INTEGER = new DataType<>(DataWatcherRegistry.b);
    public static final DataType<Float> FLOAT = new DataType<>(DataWatcherRegistry.c);
    public static final DataType<String> STRING = new DataType<>(DataWatcherRegistry.d);
    public static final DataType<IChatBaseComponent> CHAT = new DataType<>(DataWatcherRegistry.e);
    public static final DataType<Optional<IChatBaseComponent>> OPT_CHAT = new DataType<Optional<IChatBaseComponent>>(DataWatcherRegistry.f);
    public static final DataType<ItemStack> ITEM_STACK = new DataType<>(DataWatcherRegistry.g);
    public static final DataType<Optional<IBlockData>> OPT_BLOCK = new DataType<>(DataWatcherRegistry.h);
    public static final DataType<Boolean> BOOLEAN = new DataType<>(DataWatcherRegistry.i);
    public static final DataType<ParticleParam> PARTICLE_PARAM = new DataType<>(DataWatcherRegistry.j);
    public static final DataType<Vector3f> ROTATION = new DataType<>(DataWatcherRegistry.k);
    public static final DataType<BlockPosition> POSITION = new DataType<>(DataWatcherRegistry.l);
    public static final DataType<Optional<BlockPosition>> OPT_POSITION = new DataType<>(DataWatcherRegistry.m);
    public static final DataType<EnumDirection> DIRECTION = new DataType<>(DataWatcherRegistry.n);
    public static final DataType<Optional<UUID>> OPT_UUID = new DataType<>(DataWatcherRegistry.o);
    public static final DataType<NBTTagCompound> COMPOUND = new DataType<>(DataWatcherRegistry.p);

    public final DataWatcherSerializer<T> serializer;

    DataType(DataWatcherSerializer<T> serializer) {
        this.serializer = serializer;
    }
}
