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

import com.simibubi.create.foundation.data.AssetLookup;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import plus.dragons.createintegratedfarming.common.registry.CIFCreativeModeTabs;
import plus.dragons.createintegratedfarming.integration.untitledduck.ranching.roost.duck.DuckRoostBlock;

import static plus.dragons.createintegratedfarming.common.CIFCommon.REGISTRATE;
import static plus.dragons.createintegratedfarming.common.registry.CIFBlocks.ROOST;

public class UntitledDuckBlocks {
    public static final BlockEntry<DuckRoostBlock> DUCK_ROOST_NORMAL = registerDuck("duck_roost_normal",0);
    public static final BlockEntry<DuckRoostBlock> DUCK_ROOST_FEMALE = registerDuck("duck_roost_female",1);
    public static final BlockEntry<DuckRoostBlock> DUCK_ROOST_CAMPBELL = registerDuck("duck_roost_campbell",2);
    public static final BlockEntry<DuckRoostBlock> DUCK_ROOST_PEKIN = registerDuck("duck_roost_pekin",3);

    private static BlockEntry<DuckRoostBlock> registerDuck(String path, int variant){
        return REGISTRATE.block(path, prop -> new DuckRoostBlock(prop, ROOST,variant))
                .lang("Duck Roost")
                .properties(prop -> prop.strength(1.5F).sound(SoundType.BAMBOO_WOOD))
                .blockstate((ctx, prov) -> prov.horizontalBlock(ctx.get(), AssetLookup.standardModel(ctx, prov)))
                .item()
                .build()
                .register();
    }

    @SubscribeEvent
    public static void addToCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CIFCreativeModeTabs.BASE.getKey()) {
            event.accept(DUCK_ROOST_NORMAL);
            event.accept(DUCK_ROOST_FEMALE);
            event.accept(DUCK_ROOST_CAMPBELL);
            event.accept(DUCK_ROOST_PEKIN);
        }
    }

    public static void register(IEventBus modBus) {
        modBus.register(UntitledDuckBlocks.class);
    }
}
