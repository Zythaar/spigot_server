package net.minecraft.server;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import org.bukkit.event.block.CauldronLevelChangeEvent; // CraftBukkit

public class BlockCauldron extends Block {

    public static final BlockStateInteger LEVEL = BlockStateInteger.of("level", 0, 3);
    protected static final AxisAlignedBB b = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.3125D, 1.0D);
    protected static final AxisAlignedBB c = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.125D);
    protected static final AxisAlignedBB d = new AxisAlignedBB(0.0D, 0.0D, 0.875D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB e = new AxisAlignedBB(0.875D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB f = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.125D, 1.0D, 1.0D);

    public BlockCauldron() {
        super(Material.ORE, MaterialMapColor.n);
        this.w(this.blockStateList.getBlockData().set(BlockCauldron.LEVEL, Integer.valueOf(0)));
    }

    public void a(IBlockData iblockdata, World world, BlockPosition blockposition, AxisAlignedBB axisalignedbb, List<AxisAlignedBB> list, @Nullable Entity entity, boolean flag) {
        a(blockposition, axisalignedbb, list, BlockCauldron.b);
        a(blockposition, axisalignedbb, list, BlockCauldron.f);
        a(blockposition, axisalignedbb, list, BlockCauldron.c);
        a(blockposition, axisalignedbb, list, BlockCauldron.e);
        a(blockposition, axisalignedbb, list, BlockCauldron.d);
    }

    public AxisAlignedBB b(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition) {
        return BlockCauldron.j;
    }

    public boolean b(IBlockData iblockdata) {
        return false;
    }

    public boolean c(IBlockData iblockdata) {
        return false;
    }

    public void a(World world, BlockPosition blockposition, IBlockData iblockdata, Entity entity) {
        int i = ((Integer) iblockdata.get(BlockCauldron.LEVEL)).intValue();
        float f = (float) blockposition.getY() + (6.0F + (float) (3 * i)) / 16.0F;

        if (!world.isClientSide && entity.isBurning() && i > 0 && entity.getBoundingBox().b <= (double) f) {
            // CraftBukkit start
            if (!this.changeLevel(world, blockposition, iblockdata, i - 1, entity, CauldronLevelChangeEvent.ChangeReason.EXTINGUISH)) {
                return;
            }
            entity.extinguish();
            // this.a(world, blockposition, iblockdata, i - 1);
            // CraftBukkit end
        }

    }

    public boolean interact(World world, BlockPosition blockposition, IBlockData iblockdata, EntityHuman entityhuman, EnumHand enumhand, EnumDirection enumdirection, float f, float f1, float f2) {
        ItemStack itemstack = entityhuman.b(enumhand);

        if (itemstack.isEmpty()) {
            return true;
        } else {
            int i = ((Integer) iblockdata.get(BlockCauldron.LEVEL)).intValue();
            Item item = itemstack.getItem();

            if (item == Items.WATER_BUCKET) {
                if (i < 3 && !world.isClientSide) {
                    // CraftBukkit start
                    if (!this.changeLevel(world, blockposition, iblockdata, 3, entityhuman, CauldronLevelChangeEvent.ChangeReason.BUCKET_EMPTY)) {
                        return true;
                    }
                    if (!entityhuman.abilities.canInstantlyBuild) {
                        entityhuman.a(enumhand, new ItemStack(Items.BUCKET));
                    }

                    entityhuman.b(StatisticList.I);
                    // this.a(world, blockposition, iblockdata, 3);
                    // CraftBukkit end
                    world.a((EntityHuman) null, blockposition, SoundEffects.Q, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }

                return true;
            } else if (item == Items.BUCKET) {
                if (i == 3 && !world.isClientSide) {
                    // CraftBukkit start
                    if (!this.changeLevel(world, blockposition, iblockdata, 0, entityhuman, CauldronLevelChangeEvent.ChangeReason.BUCKET_FILL)) {
                        return true;
                    }
                    if (!entityhuman.abilities.canInstantlyBuild) {
                        itemstack.subtract(1);
                        if (itemstack.isEmpty()) {
                            entityhuman.a(enumhand, new ItemStack(Items.WATER_BUCKET));
                        } else if (!entityhuman.inventory.pickup(new ItemStack(Items.WATER_BUCKET))) {
                            entityhuman.drop(new ItemStack(Items.WATER_BUCKET), false);
                        }
                    }

                    entityhuman.b(StatisticList.J);
                    // this.a(world, blockposition, iblockdata, 0);
                    // CraftBukkit end
                    world.a((EntityHuman) null, blockposition, SoundEffects.S, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }

                return true;
            } else {
                ItemStack itemstack1;

                if (item == Items.GLASS_BOTTLE) {
                    if (i > 0 && !world.isClientSide) {
                        // CraftBukkit start
                        if (!this.changeLevel(world, blockposition, iblockdata, i - 1, entityhuman, CauldronLevelChangeEvent.ChangeReason.BOTTLE_FILL)) {
                            return true;
                        }
                        if (!entityhuman.abilities.canInstantlyBuild) {
                            itemstack1 = PotionUtil.a(new ItemStack(Items.POTION), Potions.b);
                            entityhuman.b(StatisticList.J);
                            itemstack.subtract(1);
                            if (itemstack.isEmpty()) {
                                entityhuman.a(enumhand, itemstack1);
                            } else if (!entityhuman.inventory.pickup(itemstack1)) {
                                entityhuman.drop(itemstack1, false);
                            } else if (entityhuman instanceof EntityPlayer) {
                                ((EntityPlayer) entityhuman).updateInventory(entityhuman.defaultContainer);
                            }
                        }

                        world.a((EntityHuman) null, blockposition, SoundEffects.N, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        // this.a(world, blockposition, iblockdata, i - 1);
                        // CraftBukkit end
                    }

                    return true;
                } else if (item == Items.POTION && PotionUtil.d(itemstack) == Potions.b) {
                    if (i < 3 && !world.isClientSide) {
                        // CraftBukkit start
                        if (!this.changeLevel(world, blockposition, iblockdata, i + 1, entityhuman, CauldronLevelChangeEvent.ChangeReason.BOTTLE_EMPTY)) {
                            return true;
                        }
                        if (!entityhuman.abilities.canInstantlyBuild) {
                            itemstack1 = new ItemStack(Items.GLASS_BOTTLE);
                            entityhuman.b(StatisticList.J);
                            entityhuman.a(enumhand, itemstack1);
                            if (entityhuman instanceof EntityPlayer) {
                                ((EntityPlayer) entityhuman).updateInventory(entityhuman.defaultContainer);
                            }
                        }

                        world.a((EntityHuman) null, blockposition, SoundEffects.M, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        // this.a(world, blockposition, iblockdata, i + 1);
                        // CraftBukkit end
                    }

                    return true;
                } else {
                    if (i > 0 && item instanceof ItemArmor) {
                        ItemArmor itemarmor = (ItemArmor) item;

                        if (itemarmor.d() == ItemArmor.EnumArmorMaterial.LEATHER && itemarmor.e_(itemstack) && !world.isClientSide) {
                            // CraftBukkit start
                            if (!this.changeLevel(world, blockposition, iblockdata, i - 1, entityhuman, CauldronLevelChangeEvent.ChangeReason.ARMOR_WASH)) {
                                return true;
                            }
                            itemarmor.d(itemstack);
                            // this.a(world, blockposition, iblockdata, i - 1);
                            // CraftBukkit end
                            entityhuman.b(StatisticList.K);
                            return true;
                        }
                    }

                    if (i > 0 && item instanceof ItemBanner) {
                        if (TileEntityBanner.b(itemstack) > 0 && !world.isClientSide) {
                            // CraftBukkit start
                            if (!this.changeLevel(world, blockposition, iblockdata, i - 1, entityhuman, CauldronLevelChangeEvent.ChangeReason.BANNER_WASH)) {
                                return true;
                            }
                            itemstack1 = itemstack.cloneItemStack();
                            itemstack1.setCount(1);
                            TileEntityBanner.c(itemstack1);
                            entityhuman.b(StatisticList.L);
                            if (!entityhuman.abilities.canInstantlyBuild) {
                                itemstack.subtract(1);
                                // this.a(world, blockposition, iblockdata, i - 1);
                                // CraftBukkit end
                            }

                            if (itemstack.isEmpty()) {
                                entityhuman.a(enumhand, itemstack1);
                            } else if (!entityhuman.inventory.pickup(itemstack1)) {
                                entityhuman.drop(itemstack1, false);
                            } else if (entityhuman instanceof EntityPlayer) {
                                ((EntityPlayer) entityhuman).updateInventory(entityhuman.defaultContainer);
                            }
                        }

                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
    }

    // CraftBukkit start
    public void a(World world, BlockPosition blockposition, IBlockData iblockdata, int i) {
        this.changeLevel(world, blockposition, iblockdata, i, null, CauldronLevelChangeEvent.ChangeReason.UNKNOWN);
    }

    private boolean changeLevel(World world, BlockPosition blockposition, IBlockData iblockdata, int i, Entity entity, CauldronLevelChangeEvent.ChangeReason reason) {
        int newLevel = Integer.valueOf(MathHelper.clamp(i, 0, 3));
        CauldronLevelChangeEvent event = new CauldronLevelChangeEvent(
                world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()),
                (entity == null) ? null : entity.getBukkitEntity(), reason, iblockdata.get(BlockCauldron.LEVEL), newLevel
        );
        world.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return false;
        }
        world.setTypeAndData(blockposition, iblockdata.set(BlockCauldron.LEVEL, event.getNewLevel()), 2);
        world.updateAdjacentComparators(blockposition, this);
        return true;
        // CraftBukkit end
    }

    public void h(World world, BlockPosition blockposition) {
        if (world.random.nextInt(20) == 1) {
            float f = world.getBiome(blockposition).a(blockposition);

            if (world.getWorldChunkManager().a(f, blockposition.getY()) >= 0.15F) {
                IBlockData iblockdata = world.getType(blockposition);

                if (((Integer) iblockdata.get(BlockCauldron.LEVEL)).intValue() < 3) {
                    this.a(world, blockposition, iblockdata.a((IBlockState) BlockCauldron.LEVEL), 2); // CraftBukkit
                }

            }
        }
    }

    public Item getDropType(IBlockData iblockdata, Random random, int i) {
        return Items.CAULDRON;
    }

    public ItemStack a(World world, BlockPosition blockposition, IBlockData iblockdata) {
        return new ItemStack(Items.CAULDRON);
    }

    public boolean isComplexRedstone(IBlockData iblockdata) {
        return true;
    }

    public int c(IBlockData iblockdata, World world, BlockPosition blockposition) {
        return ((Integer) iblockdata.get(BlockCauldron.LEVEL)).intValue();
    }

    public IBlockData fromLegacyData(int i) {
        return this.getBlockData().set(BlockCauldron.LEVEL, Integer.valueOf(i));
    }

    public int toLegacyData(IBlockData iblockdata) {
        return ((Integer) iblockdata.get(BlockCauldron.LEVEL)).intValue();
    }

    protected BlockStateList getStateList() {
        return new BlockStateList(this, new IBlockState[] { BlockCauldron.LEVEL});
    }

    public boolean b(IBlockAccess iblockaccess, BlockPosition blockposition) {
        return true;
    }

    public EnumBlockFaceShape a(IBlockAccess iblockaccess, IBlockData iblockdata, BlockPosition blockposition, EnumDirection enumdirection) {
        return enumdirection == EnumDirection.UP ? EnumBlockFaceShape.BOWL : (enumdirection == EnumDirection.DOWN ? EnumBlockFaceShape.UNDEFINED : EnumBlockFaceShape.SOLID);
    }
}
