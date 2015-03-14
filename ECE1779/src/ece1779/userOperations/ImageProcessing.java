package ece1779.userOperations;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.process.ProcessStarter;

public class ImageProcessing {
	/**
	 * local : "/opt/local/bin"; linux : /usr/bin
	 */
	private final static String imPath = "/usr/bin";
	static {
		ProcessStarter.setGlobalSearchPath(imPath);
	}

	/**
	 * Get transformed pics with the original one.
	 * 
	 * @param file
	 * @return
	 * @throws IM4JavaException
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public List<File> transform(File file) throws IOException,
			InterruptedException, IM4JavaException {
		System.out.println(file.getAbsolutePath());
		List<File> images = new ArrayList<File>();
		images.add(file);
		images.add(flop(file));
		images.add(monochrome(file));
		images.add(negate(file));

		return images;

	}

	private File monochrome(File file) throws IOException,
			InterruptedException, IM4JavaException {
		String path = file.getParent() + "/" + UUID.randomUUID();
		ConvertCmd cmd = new ConvertCmd();
		IMOperation operation = new IMOperation();
		operation.addImage(file.getCanonicalPath());
		operation.monochrome();
		operation.addImage(path);
		cmd.run(operation);
		return new File(path);
	}

	private File negate(File file) throws IOException, InterruptedException,
			IM4JavaException {
		String path = file.getParent() + "/" + UUID.randomUUID();
		IMOperation op = new IMOperation();
		op.addImage(file.getCanonicalPath());
		op.negate();

		op.addImage(path);

		ConvertCmd convert = new ConvertCmd();
		convert.run(op);
		return new File(path);

	}

	private File flop(File file) throws IOException, InterruptedException,
			IM4JavaException {
		String path = file.getParent() + "/" + UUID.randomUUID();
		IMOperation op = new IMOperation();
		op.addImage(file.getCanonicalPath());
		op.flop();
		op.addImage(path);
		ConvertCmd convert = new ConvertCmd();
		convert.run(op);
		return new File(path);
	}

	public static void main(String[] args) {
		try {
			ImageProcessing ip = new ImageProcessing();
			File file = new File(
					"/Users/liuwyang/Dropbox/ECE1779_WORKSPACE/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/ECE1779/a6e6500d-8c78-4419-b54f-2fcfa28fd466");
			ip.transform(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IM4JavaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
