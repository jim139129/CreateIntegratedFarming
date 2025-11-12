package plus.dragons.createintegratedfarming.mixin.delightoflight;

import me.fallenbreath.conditionalmixin.api.mixin.RestrictiveMixinConfigPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class DFMixinPlugin extends RestrictiveMixinConfigPlugin {
    @Override
    @Nullable
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    @Nullable
    public List<String> getMixins() {
        return null;
    }
}
