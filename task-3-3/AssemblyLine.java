import java.util.ArrayList;
import java.util.Arrays;

public class AssemblyLine implements IAssemblyLine {
    private ArrayList<ILineStep> steps = new ArrayList<ILineStep>();

    public AssemblyLine(ILineStep... steps) {
        this.steps.addAll(Arrays.asList(steps));
    }

    @Override
    public IProduct assembleProduct(IProduct product) {
        ArrayList<IProductPart> parts = new ArrayList<IProductPart>();
        for (ILineStep step : steps) {
            parts.add(step.buildProductPart());
        }
        product.InstallFirstPart(parts.get(0));
        product.InstallSecondPart(parts.get(1));
        product.InstallThirdPart(parts.get(2));
        return product;
    }
}
