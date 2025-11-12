package plus.dragons.createintegratedfarming.mixin.netherdepthupgrade;

import me.fallenbreath.conditionalmixin.api.mixin.RestrictiveMixinConfigPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class NDUMixinPlugin extends RestrictiveMixinConfigPlugin {
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
