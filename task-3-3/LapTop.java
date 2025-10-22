import java.util.ArrayList;

public class LapTop implements IProduct {
    public String name;
    public ArrayList<IProductPart> parts = new ArrayList<IProductPart>();

    public LapTop(String name) {
        this.name = name;
    }

    @Override
    public void InstallFirstPart(IProductPart productPart) {
        parts.add(productPart);
        System.out.println("1-я часть установлена в устройство.");
    }

    @Override
    public void InstallSecondPart(IProductPart productPart) {
        parts.add(productPart);
        System.out.println("2-я часть установлена в устройство.");
    }

    @Override
    public void InstallThirdPart(IProductPart productPart) {
        parts.add(productPart);
        System.out.println("3-я часть установлена в устройство.");
    }
}
