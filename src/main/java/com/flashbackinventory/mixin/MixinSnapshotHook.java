package com.flashbackinventory.mixin;

import com.flashbackinventory.ReplayPayloadHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Injects extra snapshot data into Flashback WITHOUT touching anything Flashback already records.
 * Hotbar/HUD is intentionally NOT modified (Flashback already handles that).
 *
 * This addon ONLY records the contents of any openable menu:
 *  - Player inventory (main inventory, not HUD rendering)
 *  - Shulker boxes
 *  - Single & double chests
 *  - Barrels
 *  - Ender chests
 *  - Droppers / dispensers
 *  - Hoppers
 *  - Any other ScreenHandler-backed container
 */

@Mixin(targets = "com.moulberry.flashback.recording.SnapshotCollector") // TODO: Update target if Flashback class changes
public class MixinSnapshotHook {

    @Inject(method = "addCustomSnapshotData(Lnet/minecraft/network/PacketByteBuf;)V", at = @At("RETURN"))
    private void flashbackinventory$addContainerData(PacketByteBuf buf, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        NbtCompound root = new NbtCompound();

        /*
         * Capture ANY open container via ScreenHandler.
         * This automatically covers:
         *  - chests (single/double)
         *  - barrels
         *  - shulkers
         *  - ender chest
         *  - hoppers
         *  - droppers / dispensers
         *  - modded containers
         */
        ScreenHandler handler = client.player.currentScreenHandler;
        if (handler != null) {
            NbtList slots = new NbtList();
            for (int i = 0; i < handler.slots.size(); i++) {
                Slot slot = handler.slots.get(i);
                ItemStack stack = slot.getStack();

                NbtCompound slotNbt = new NbtCompound();
                slotNbt.putInt("Slot", i);
                if (!stack.isEmpty()) {
                    slotNbt.put("Item", stack.writeNbt(new NbtCompound()));
                }
                slots.add(slotNbt);
            }

            root.putString("containerType", handler.getClass().getName());
            root.putInt("slotCount", handler.slots.size());
            root.put("containerSlots", slots);
        }

        Identifier id = new Identifier("flashbackinventory", "container_snapshot_v1");
        buf.writeBoolean(true);            // addon marker
        buf.writeIdentifier(id);           // payload id
        buf.writeNbt(root);                // payload data
    }
}
