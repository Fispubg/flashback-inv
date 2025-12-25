package com.flashbackinventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ReplayPayloadHandler {
    public static final Identifier PAYLOAD_ID = new Identifier("flashbackinventory", "container_snapshot_v1");

    public static void register() {
        // This is intentionally empty: actual wiring depends on Flashback internals.
        // If you provide the exact Flashback jar filename/version, we can update the mixin target(s)
        // or add a mixin that calls applySnapshot when Flashback dispatches the custom payload.
    }

    public static void applySnapshot(NbtCompound root, PlayerEntity player) {
        if (root == null || player == null) return;

        if (root.contains("containerSlots")) {
            NbtList list = root.getList("containerSlots", 10);
            // This method demonstrates applying to player inventories or ender chest, but
            // in practice the replay player entity should have the correct inventory/handler to apply to.
            for (int i = 0; i < list.size(); i++) {
                NbtCompound slot = list.getCompound(i);
                int index = slot.getInt("Slot");
                if (slot.contains("Item")) {
                    ItemStack stack = ItemStack.fromNbt(slot.getCompound("Item"));
                    // Best-effort: try to apply to player inventory if index in range.
                    if (index >= 0 && index < player.getInventory().size()) {
                        player.getInventory().setStack(index, stack);
                    }
                } else {
                    if (index >= 0 && index < player.getInventory().size()) {
                        player.getInventory().setStack(index, ItemStack.EMPTY);
                    }
                }
            }
        }
    }
}
