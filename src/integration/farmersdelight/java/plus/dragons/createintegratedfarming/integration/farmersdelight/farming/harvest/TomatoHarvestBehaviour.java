/*
 * Copyright (C) 2025  DragonsPlus
 * SPDX-License-Identifier: LGPL-3.0-or-later
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package plus.dragons.createintegratedfarming.integration.farmersdelight.farming.harvest;

import com.simibubi.create.content.contraptions.actors.harvester.HarvesterMovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.foundation.utility.BlockHelper;
import java.lang.reflect.Field;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;
import plus.dragons.createintegratedfarming.api.harvester.CustomHarvestBehaviour;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModSounds;

@SuppressWarnings("unchecked")
public class TomatoHarvestBehaviour implements CustomHarvestBehaviour {
    private static final Class<?> TOMATO_BLOCK_CLASS;
    private static final Class<?> TOMATO_VINE_BLOCK_CLASS;
    private static final Supplier<SoundEvent> PICK_TOMATOES_SOUND;
    private static final BooleanProperty ROPELOGGED_PROPERTY;

    static {
        Class<?> tomatoBlockClass = null;
        try {
            tomatoBlockClass = Class.forName("vectorwing.farmersdelight.common.block.TomatoBlock");
        } catch (ClassNotFoundException e) {
            // Old version before 1.3.0, TomatoBlock does not exist
        }
        TOMATO_BLOCK_CLASS = tomatoBlockClass;

        Class<?> tomatoVineBlockClass = null;
        try {
            tomatoVineBlockClass = Class.forName("vectorwing.farmersdelight.common.block.TomatoVineBlock");
        } catch (ClassNotFoundException e) {
            // Should not happen in supported versions, but handle gracefully
        }
        TOMATO_VINE_BLOCK_CLASS = tomatoVineBlockClass;

        Supplier<SoundEvent> sound = null;
        try {
            // Farmer's Delight 1.3.0+
            Field field = ModSounds.class.getField("BLOCK_TOMATOES_PICK_TOMATOES");
            sound = (Supplier<SoundEvent>) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            try {
                // Farmer's Delight < 1.3.0
                Field field = ModSounds.class.getField("ITEM_TOMATO_PICK_FROM_BUSH");
                sound = (Supplier<SoundEvent>) field.get(null);
            } catch (NoSuchFieldException | IllegalAccessException ex) {
                throw new RuntimeException("Failed to find tomato pick sound in FarmersDelight", ex);
            }
        }
        PICK_TOMATOES_SOUND = sound;

        BooleanProperty ropelogged = null;
        if (tomatoBlockClass != null) {
            try {
                Field field = tomatoBlockClass.getField("ROPELOGGED");
                ropelogged = (BooleanProperty) field.get(null);
            } catch (NoSuchFieldException | IllegalAccessException ignored) {}
        }
        if (ropelogged == null && tomatoVineBlockClass != null) {
            try {
                Field field = tomatoVineBlockClass.getField("ROPELOGGED");
                ropelogged = (BooleanProperty) field.get(null);
            } catch (NoSuchFieldException | IllegalAccessException ignored) {}
        }
        ROPELOGGED_PROPERTY = ropelogged;
    }

    private final CropBlock tomato;
    private final IntegerProperty ageProperty;

    public TomatoHarvestBehaviour(CropBlock tomato, IntegerProperty ageProperty) {
        this.tomato = tomato;
        this.ageProperty = ageProperty;
    }

    public static @Nullable TomatoHarvestBehaviour create(Block block) {
        if (TOMATO_BLOCK_CLASS != null && TOMATO_BLOCK_CLASS.isInstance(block)) {
            return new TomatoHarvestBehaviour((CropBlock) block, getAgeProperty(block));
        }
        if (TOMATO_VINE_BLOCK_CLASS != null && TOMATO_VINE_BLOCK_CLASS.isInstance(block)) {
            return new TomatoHarvestBehaviour((CropBlock) block, getAgeProperty(block));
        }
        return null;
    }

    private static IntegerProperty getAgeProperty(Block block) {
        try {
            return (IntegerProperty) block.getClass().getMethod("getAgeProperty").invoke(block);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get age property from tomato block: " + block, e);
        }
    }

    @Override
    public void harvest(HarvesterMovementBehaviour behaviour, MovementContext context, BlockPos pos, BlockState state) {
        boolean replant = CustomHarvestBehaviour.replant();
        boolean partial = CustomHarvestBehaviour.partial();
        boolean mature = tomato.getAge(state) == tomato.getMaxAge();
        Level level = context.world;
        if (!replant) {
            if (mature || partial)
                breakTomatoes(level, behaviour, context, pos, state);
            return;
        }
        if (mature) {
            dropTomatoes(level, behaviour, context);
            level.playSound(null, pos, PICK_TOMATOES_SOUND.get(), SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
            level.setBlock(pos, state.setValue(ageProperty, 0), 2);
        } else if (partial) {
            level.setBlock(pos, state.setValue(ageProperty, 0), 2);
        }
    }

    protected void breakTomatoes(Level level, HarvesterMovementBehaviour behaviour, MovementContext context, BlockPos pos, BlockState state) {
        BlockPos above = pos.above();
        BlockState stateAbove = level.getBlockState(above);
        if (stateAbove.is(tomato))
            breakTomatoes(level, behaviour, context, above, stateAbove);
        boolean ropelogged = ROPELOGGED_PROPERTY != null && state.hasProperty(ROPELOGGED_PROPERTY) && state.getValue(ROPELOGGED_PROPERTY);
        BlockHelper.destroyBlockAs(
                level,
                pos,
                null,
                CustomHarvestBehaviour.getHarvestTool(context),
                1,
                stack -> behaviour.collectOrDropItem(context, stack));
        if (ropelogged)
            level.setBlockAndUpdate(pos, getRope());
    }

    protected void dropTomatoes(Level level, HarvesterMovementBehaviour behaviour, MovementContext context) {
        behaviour.collectOrDropItem(context, new ItemStack(ModItems.TOMATO.get(), 1 + level.random.nextInt(2)));
        if (level.random.nextFloat() < 0.05)
            behaviour.collectOrDropItem(context, new ItemStack(ModItems.ROTTEN_TOMATO.get()));
    }

    protected BlockState getRope() {
        ResourceLocation ropeId = ResourceLocation.parse(Configuration.DEFAULT_TOMATO_VINE_ROPE.get());
        BlockState rope = BuiltInRegistries.BLOCK.get(ropeId).defaultBlockState();
        if (rope.isAir())
            return ModBlocks.ROPE.get().defaultBlockState();
        return rope;
    }
}
