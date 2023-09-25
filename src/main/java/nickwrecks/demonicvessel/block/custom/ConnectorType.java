package nickwrecks.demonicvessel.block.custom;

import net.minecraft.util.StringRepresentable;

public enum ConnectorType implements StringRepresentable {
    NONE,
    CABLED,
    CONNECTED;

    public static final ConnectorType[] VALUES = values();


    @Override
    public String getSerializedName() {
        return name().toLowerCase();
    }
}
