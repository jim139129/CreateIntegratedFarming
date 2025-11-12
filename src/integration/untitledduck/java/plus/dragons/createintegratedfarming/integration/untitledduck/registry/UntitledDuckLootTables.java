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

package plus.dragons.createintegratedfarming.integration.untitledduck.registry;

import com.tterrag.registrate.providers.loot.RegistrateLootTableProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.untitledduckmod.common.init.ModItems;
import plus.dragons.createintegratedfarming.common.CIFCommon;

import java.util.function.BiConsumer;

public class UntitledDuckLootTables {
    // Manually write loot table since it needs mod-loading condition.
    public static final ResourceKey<LootTable> DUCK_ROOST = ResourceKey
            .create(Registries.LOOT_TABLE, CIFCommon.asResource("gameplay/roost/duck"));
    public static final ResourceKey<LootTable> GOOSE_ROOST = ResourceKey
            .create(Registries.LOOT_TABLE, CIFCommon.asResource("gameplay/roost/goose"));
}
