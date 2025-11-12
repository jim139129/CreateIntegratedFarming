package plus.dragons.createintegratedfarming.integration.untitledduck;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.fml.loading.FMLLoader;
import plus.dragons.createintegratedfarming.common.CIFCommon;
import plus.dragons.createintegratedfarming.integration.ModIntegration;
import plus.dragons.createintegratedfarming.integration.untitledduck.registry.UntitledDuckBlockEntities;
import plus.dragons.createintegratedfarming.integration.untitledduck.registry.UntitledDuckBlocks;
import plus.dragons.createintegratedfarming.integration.untitledduck.registry.UntitledRoostCapturables;

@Mod(CIFCommon.ID)
public class UntitledDuckIntegration {
    public UntitledDuckIntegration(IEventBus modBus) {
        if (ModIntegration.UNTITLED_DUCK.enabled()) {
            modBus.register(new Common(modBus));
            if (FMLLoader.getDist() == Dist.CLIENT)
                modBus.register(new Client());
        }
    }

    public static class Common {
        public Common(IEventBus modBus) {
            this.modBus = modBus;
        }

        IEventBus modBus;

        @SubscribeEvent
        public void construct(final FMLConstructModEvent event) {
            UntitledDuckBlocks.register(modBus);
            UntitledDuckBlockEntities.register(modBus);
        }

        @SubscribeEvent
        public void commonSetup(final FMLCommonSetupEvent event) {
            event.enqueueWork(UntitledRoostCapturables::register);
        }
    }

    public static class Client {
        @SubscribeEvent
        public void construct(final FMLConstructModEvent event) {
            //FDPonderPlugin.register();
        }
    }
}
