package de.tsgscraft.advancedclothing.mixin;

import de.tsgscraft.advancedclothing.REFERENCE;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.LoadingModList;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class AdvancedClothingMixinPlugin implements IMixinConfigPlugin {

    private boolean fmgModLoaded;

    @Override
    public void onLoad(String mixinPackage) {
        fmgModLoaded = LoadingModList.get().getModFileById(REFERENCE.fmg_MODID) != null;
        System.out.println("FMG mod loaded: " + fmgModLoaded);
    }

    @Override
    public String getRefMapperConfig() {
        return "";
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        System.out.println("Checking if mixin should be applied: " + mixinClassName + " for target class: " + targetClassName);
        if (mixinClassName.equals("de.tsgscraft.advancedclothing.mixin.fmg.GenderLayerMixin")) {
            return fmgModLoaded;
        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
