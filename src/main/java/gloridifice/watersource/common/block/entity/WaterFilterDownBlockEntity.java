package gloridifice.watersource.common.block.entity;

import gloridifice.watersource.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;

public class WaterFilterDownBlockEntity extends ModNormalBlockEntity {
    LazyOptional<FluidTank> downTank = LazyOptional.of(this::createFluidHandler);

    double cacheTimeEnter, cacheTimeExit = 0;
    boolean previousIsIn = false;
    boolean isLeftAnimeEnd = true;

    final int capacity = 3000;
    public WaterFilterDownBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.WATER_FILTER_DOWN_TILE.get(), pos, state);
    }

    public double getCacheTimeEnter() {
        return cacheTimeEnter;
    }

    public void setCacheTimeEnter(double cacheTimeEnter) {
        this.cacheTimeEnter = cacheTimeEnter;
    }

    public double getCacheTimeExit() {
        return cacheTimeExit;
    }

    public void setCacheTimeExit(double cacheTimeExit) {
        this.cacheTimeExit = cacheTimeExit;
    }

    public boolean isPreviousIsIn() {
        return previousIsIn;
    }

    public void setPreviousIsIn(boolean previousIsIn) {
        this.previousIsIn = previousIsIn;
    }

    public boolean isLeftAnimeEnd() {
        return isLeftAnimeEnd;
    }

    public void setLeftAnimeEnd(boolean leftAnimeEnd) {
        isLeftAnimeEnd = leftAnimeEnd;
    }

    public LazyOptional<FluidTank> getDownTank() {
        return downTank;
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        downTank.ifPresent(fluidTank -> fluidTank.readFromNBT(compound.getCompound("downTank")));
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        downTank.ifPresent(fluidTank -> compound.put("downTank", fluidTank.writeToNBT(new CompoundTag())));
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
        if (!this.isRemoved()) {
            if (CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.equals(cap)) {
                return downTank.cast();
            }
        }
        return super.getCapability(cap, side);
    }

    private FluidTank createFluidHandler() {
        return new FluidTank(capacity) {
            @Override
            protected void onContentsChanged() {
                setChanged();
                clientSync();
            }

            @Override
            public boolean isFluidValid(FluidStack stack) {
                return !stack.getFluid().getAttributes().isLighterThanAir() && stack.getFluid().getAttributes().getTemperature() < 500;
            }
        };
    }
}
