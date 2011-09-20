package source;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.layout.FillLayout;

public class DisplayUI {

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			DisplayUI window = new DisplayUI();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private static Shell shell;
	public static Shell getShell() {
		return shell;
	}
	
	private static Canvas canvas;
	public static Canvas getCanvas() {
		return canvas;
	}

	

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		shell = new Shell();
		shell.setSize(800, 600);
		shell.setText("SWT Application");
		GridLayout gl_shell = new GridLayout(1, false);
		gl_shell.marginHeight = 0;
		gl_shell.verticalSpacing = 0;
		gl_shell.marginWidth = 0;
		shell.setLayout(gl_shell);
		
		ToolBar toolBar = new ToolBar(shell, SWT.FLAT | SWT.RIGHT);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		
		ToolItem tltmStart = new ToolItem(toolBar, SWT.NONE);
		tltmStart.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		tltmStart.setText("Start");
		
		ToolItem tltmPause = new ToolItem(toolBar, SWT.NONE);
		tltmPause.setText("Pause");
		
		ToolItem tltmStop = new ToolItem(toolBar, SWT.NONE);
		tltmStop.setText("Stop");
		
		ToolItem tltmSettings = new ToolItem(toolBar, SWT.NONE);
		
		tltmSettings.setText("Settings");
		
		Composite composite = new Composite(shell, SWT.NONE);
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.verticalSpacing = 0;
		gl_composite.marginWidth = 0;
		gl_composite.marginHeight = 0;
		composite.setLayout(gl_composite);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		canvas = new Canvas(composite, SWT.DOUBLE_BUFFERED);
		canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		canvas.setLayout(new FillLayout(SWT.HORIZONTAL));

		shell.open();
		shell.layout();
		Experiment.main(null);
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
}
