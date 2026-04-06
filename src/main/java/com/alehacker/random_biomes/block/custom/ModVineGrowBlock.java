package com.alehacker.random_biomes.block.custom;

import com.alehacker.random_biomes.block.ModBlocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;

public class ModVineGrowBlock extends PipeBlock {

    private static final int GROWTH_CHANCE_BOUND = 4;
    private static final int MAX_NETWORK_NODES = 48;
    private static final int MAX_HEIGHT_ABOVE_SUPPORT = 9;
    private static final int MAX_CONNECTED_VINES = 32;
    private static final float HORIZONTAL_GROWTH_FRACTION = 0.3f;

    public static final BooleanProperty UP = BlockStateProperties.UP;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    public static final BooleanProperty CAN_GROW = BooleanProperty.create("can_grow");

    public ModVineGrowBlock(Properties properties) {
        super(0.3125F, properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(UP, false).setValue(DOWN, false)
                .setValue(NORTH, false).setValue(EAST, false)
                .setValue(SOUTH, false).setValue(WEST, false)
                .setValue(CAN_GROW, false));
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return state.getValue(CAN_GROW);
    }

    private static boolean isSupportBlock(BlockState state) {
        return state.is(ModBlocks.CRIMSON_GRASS.get()) || state.is(ModBlocks.CRIMSON_STONE.get());
    }

    private boolean hasNetworkToSupport(LevelReader level, BlockPos start) {
        if (isSupportBlock(level.getBlockState(start.below()))) {
            return true;
        }

        ArrayDeque<BlockPos> queue = new ArrayDeque<>();
        HashSet<BlockPos> vinesSeen = new HashSet<>();

        if (level.getBlockState(start).is(this)) {
            queue.add(start);
        } else {
            for (Direction d : Direction.values()) {
                BlockPos n = start.relative(d);
                if (level.getBlockState(n).is(this)) {
                    queue.add(n);
                }
            }
        }

        while (!queue.isEmpty()) {
            BlockPos p = queue.removeFirst();
            if (!vinesSeen.add(p)) {
                continue;
            }
            if (vinesSeen.size() > MAX_NETWORK_NODES) {
                return false;
            }
            if (isSupportBlock(level.getBlockState(p.below()))) {
                return true;
            }
            for (Direction d : Direction.values()) {
                BlockPos n = p.relative(d);
                if (level.getBlockState(n).is(this) && !vinesSeen.contains(n)) {
                    queue.addLast(n);
                }
            }
        }
        return false;
    }

    private HashSet<BlockPos> connectedVineComponent(LevelReader level, BlockPos start) {
        HashSet<BlockPos> vines = new HashSet<>();
        if (!level.getBlockState(start).is(this)) {
            return vines;
        }
        ArrayDeque<BlockPos> queue = new ArrayDeque<>();
        queue.add(start);
        while (!queue.isEmpty()) {
            BlockPos p = queue.removeFirst();
            if (!level.getBlockState(p).is(this) || !vines.add(p)) {
                continue;
            }
            if (vines.size() > MAX_NETWORK_NODES) {
                break;
            }
            for (Direction d : Direction.values()) {
                queue.addLast(p.relative(d));
            }
        }
        return vines;
    }

    private boolean withinHeightLimit(LevelReader level, BlockPos source, BlockPos target) {
        HashSet<BlockPos> vines = connectedVineComponent(level, source);
        int minSupportY = Integer.MAX_VALUE;
        for (BlockPos p : vines) {
            BlockPos below = p.below();
            if (isSupportBlock(level.getBlockState(below))) {
                minSupportY = Math.min(minSupportY, below.getY());
            }
        }
        if (minSupportY == Integer.MAX_VALUE) {
            return true;
        }
        int maxVineY = target.getY();
        for (BlockPos p : vines) {
            maxVineY = Math.max(maxVineY, p.getY());
        }
        return maxVineY > minSupportY + MAX_HEIGHT_ABOVE_SUPPORT;
    }

    private boolean isBaseVine(LevelReader level, BlockPos pos) {
        return isSupportBlock(level.getBlockState(pos.below()));
    }

    private int countVineNeighbors(LevelReader level, BlockPos pos) {
        int count = 0;
        for (Direction d : Direction.values()) {
            if (level.getBlockState(pos.relative(d)).is(this)) {
                count++;
            }
        }
        return count;
    }

    private boolean isTipVine(LevelReader level, BlockPos pos) {
        if (!level.getBlockState(pos).is(this)) {
            return false;
        }
        return countVineNeighbors(level, pos) <= 1;
    }

    private @Nullable Direction pickGrowthDirection(RandomSource random, LevelReader level, BlockPos pos) {
        boolean upClear = level.isEmptyBlock(pos.above());
        if (random.nextFloat() >= HORIZONTAL_GROWTH_FRACTION) {
            return upClear ? Direction.UP : null;
        }
        if (isBaseVine(level, pos)) {
            return upClear ? Direction.UP : null;
        }
        List<Direction> horizontal = Direction.Plane.HORIZONTAL.shuffledCopy(random);
        for (Direction d : horizontal) {
            BlockPos first = pos.relative(d);
            BlockPos second = first.relative(d);
            if (level.isEmptyBlock(first) && level.isEmptyBlock(second)) {
                return d;
            }
        }
        return upClear ? Direction.UP : null;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState below = level.getBlockState(pos.below());
        if (below.is(this) || isSupportBlock(below)) {
            return hasNetworkToSupport(level, pos);
        }
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            if (level.getBlockState(pos.relative(dir)).is(this)) {
                return hasNetworkToSupport(level, pos);
            }
        }
        return false;
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity) {
            entity.hurt(level.damageSources().cactus(), 1.0F);
        }
    }

    @Override
    protected @NotNull VoxelShape getCollisionShape(
            BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockPos pos = ctx.getClickedPos();
        LevelReader level = ctx.getLevel();
        BlockState placed = makeConnections(level, pos, true);
        return placed.canSurvive(level, pos) ? placed : null;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (!state.getValue(CAN_GROW) || random.nextInt(GROWTH_CHANCE_BOUND) != 0) {
            return;
        }
        Direction dir = pickGrowthDirection(random, world, pos);
        if (dir == null) {
            return;
        }
        if (dir.getAxis().isHorizontal()) {
            tryGrowHorizontalTwo(world, pos, dir);
        } else {
            tryGrowSingle(world, pos, dir);
        }
    }

    private void tryGrowSingle(ServerLevel world, BlockPos pos, Direction dir) {
        BlockPos targetPos = pos.relative(dir);
        if (!world.isEmptyBlock(targetPos)) {
            return;
        }
        if (withinHeightLimit(world, pos, targetPos)) {
            return;
        }
        if (!defaultBlockState().canSurvive(world, targetPos)) {
            return;
        }
        boolean posCanGrow = world.getBlockState(pos).getValue(CAN_GROW);
        world.setBlockAndUpdate(targetPos, makeConnections(world, targetPos, true));
        world.setBlockAndUpdate(pos, makeConnections(world, pos, posCanGrow));
        checkGrowthLimits(world, targetPos);
    }

    private void tryGrowHorizontalTwo(ServerLevel world, BlockPos pos, Direction dir) {
        BlockPos first = pos.relative(dir);
        BlockPos second = first.relative(dir);
        if (!world.isEmptyBlock(first) || !world.isEmptyBlock(second)) {
            return;
        }
        if (withinHeightLimit(world, pos, second)) {
            return;
        }
        if (!defaultBlockState().canSurvive(world, first)) {
            return;
        }
        boolean posCanGrow = world.getBlockState(pos).getValue(CAN_GROW);
        world.setBlockAndUpdate(first, makeConnections(world, first, false));
        if (!defaultBlockState().canSurvive(world, second)) {
            world.setBlockAndUpdate(first, Blocks.AIR.defaultBlockState());
            return;
        }
        world.setBlockAndUpdate(second, makeConnections(world, second, true));
        world.setBlockAndUpdate(first, makeConnections(world, first, false));
        world.setBlockAndUpdate(pos, makeConnections(world, pos, posCanGrow));
        checkGrowthLimits(world, second);
    }

    private void checkGrowthLimits(ServerLevel world, BlockPos pos) {
        HashSet<BlockPos> vines = connectedVineComponent(world, pos);
        if (vines.size() >= MAX_CONNECTED_VINES) {
            for (BlockPos vinePos : vines) {
                BlockState state = world.getBlockState(vinePos);
                if (state.is(this) && state.getValue(CAN_GROW)) {
                    world.setBlockAndUpdate(vinePos, state.setValue(CAN_GROW, false));
                }
            }
        } else {
            for (BlockPos vinePos : vines) {
                BlockState state = world.getBlockState(vinePos);
                if (state.is(this) && !state.getValue(CAN_GROW) && isTipVine(world, vinePos)) {
                    world.setBlockAndUpdate(vinePos, state.setValue(CAN_GROW, true));
                }
            }
        }
    }

    private boolean connectsOnFace(BlockGetter level, BlockPos pos, Direction direction) {
        BlockState neighbor = level.getBlockState(pos.relative(direction));
        if (neighbor.is(this)) {
            return true;
        }
        return direction == Direction.DOWN && isSupportBlock(neighbor);
    }

    public BlockState makeConnections(BlockGetter level, BlockPos pos, boolean canGrow) {
        BlockState state = this.defaultBlockState().setValue(CAN_GROW, canGrow);
        for (Direction direction : Direction.values()) {
            state = state.setValue(PROPERTY_BY_DIRECTION.get(direction), connectsOnFace(level, pos, direction));
        }
        return state;
    }

    @Override
    public BlockState updateShape(
            BlockState state,
            Direction direction,
            BlockState neighborState,
            LevelAccessor level,
            BlockPos pos,
            BlockPos neighborPos) {
        if (!state.canSurvive(level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        boolean connect = neighborState.is(this)
                || (direction == Direction.DOWN && isSupportBlock(neighborState));
        state = state.setValue(PROPERTY_BY_DIRECTION.get(direction), connect);
        if (level instanceof ServerLevel serverLevel) {
            reEnableTipVines(serverLevel, pos);
        }
        return state;
    }

    private void reEnableTipVines(ServerLevel world, BlockPos pos) {
        HashSet<BlockPos> vines = connectedVineComponent(world, pos);
        if (vines.size() < MAX_CONNECTED_VINES) {
            for (BlockPos vinePos : vines) {
                BlockState state = world.getBlockState(vinePos);
                if (state.is(this) && !state.getValue(CAN_GROW) && isTipVine(world, vinePos)) {
                    world.setBlockAndUpdate(vinePos, state.setValue(CAN_GROW, true));
                }
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(UP, DOWN, NORTH, EAST, SOUTH, WEST, CAN_GROW);
    }

    @Override
    public @NotNull MapCodec<ModVineGrowBlock> codec() {
        return simpleCodec(ModVineGrowBlock::new);
    }
}
