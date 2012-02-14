package at.mkw.android.ui;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Environment;
import android.os.SystemClock;
import android.util.StateSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class FolderPicker extends Dialog implements OnItemClickListener, OnClickListener {

	private ListView mFolders;
	private TextView mCurrentFolder;
	private Folder mPath;
	private Folder mFilePath;
	private File mRootSDCard;
	private FolderAdapter mAdapter;
	private OnClickListener mListener;
	private boolean mAcceptFiles;
	private View mOkButton;

	public FolderPicker(Context context, OnClickListener listener, int themeId) {
		this(context, listener, themeId, false);
	}

	public FolderPicker(Context context, OnClickListener listener, int themeId, boolean acceptFiles) {
		super(context, themeId);
		mListener = listener;
		mAcceptFiles = acceptFiles;
		setTitle(R.string.folder_button_text);
		setContentView(R.layout.folders);

		mRootSDCard = Environment.getExternalStorageDirectory();
		
		mOkButton = findViewById(R.id.ok_btn);
		mOkButton.setOnClickListener(this);
		findViewById(R.id.cancel_btn).setOnClickListener(this);
		mCurrentFolder = (TextView) findViewById(R.id.current_folder);
		mCurrentFolder.setSelected(true);
		mFolders = (ListView) findViewById(R.id.folders);
		mFolders.setOnItemClickListener(this);

		Animation animation = new AlphaAnimation(0, 1);
		animation.setDuration(250);
		LayoutAnimationController controller = new LayoutAnimationController(animation);
		mFolders.setLayoutAnimation(controller);
		
		mAdapter = new FolderAdapter();
		mFolders.setAdapter(mAdapter);
		mPath = new Folder(mRootSDCard.getAbsolutePath());
		updateAdapter();
	}

	public String getPath() {
		if (!mAcceptFiles) {
			return mPath.getAbsolutePath();
		} else
		if (mFilePath != null) {
			return mFilePath.getAbsolutePath();
		} else {
			return null;
		}
	}

	public void onClick(View v) {
		if (v == mOkButton && mListener != null) {
			mListener.onClick(this, DialogInterface.BUTTON_POSITIVE);
		}
		dismiss();
	}

	private void updateAdapter() {
		mCurrentFolder.setText(mPath.getAbsolutePath());
		mAdapter.clear();
		if (!mPath.equals(mRootSDCard)) {
			mAdapter.add(new Folder(mPath, true));
		}
		File[] dirs = mPath.listFiles(mDirFilter);
		Arrays.sort(dirs);
		for (int i = 0; i < dirs.length; i++) {
			mAdapter.add(new Folder(dirs[i]));
		}
		if (mAcceptFiles) {
			File[] files = mPath.listFiles(mFileFilter);
			Arrays.sort(files);
			for (int i = 0; i < files.length; i++) {
				mAdapter.add(new Folder(files[i]));
			}
		}
		mAdapter.notifyDataSetChanged();
		mFolders.setSelection(0);
		mFolders.startLayoutAnimation();
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (mAcceptFiles) {
			Folder item = (Folder) mAdapter.getItem(position);
			if (item.isDirectory()) {
				mPath = item;
				updateAdapter();
				mFilePath = null;
			} else {
				mCurrentFolder.setText(item.getAbsolutePath());
				mFilePath = item;
			}
		} else {
			mPath = (Folder) mAdapter.getItem(position);
			updateAdapter();
		}
	}

	private FileFilter mDirFilter = new FileFilter() {
		public boolean accept(File file) {
			return file.isDirectory();
		}
	};

	private FileFilter mFileFilter = new FileFilter() {
		public boolean accept(File file) {
			return file.isFile();
		}
	};
	
	class FolderAdapter extends BaseAdapter {
		ArrayList<Folder> mFolders = new ArrayList<Folder>();
		LayoutInflater mInflater = LayoutInflater.from(getContext());
		private Drawable[] mFolderUpLayers;
		private Drawable[] mFolderLayers;
		private Drawable mFileDrawable;
		
		public FolderAdapter() {
			Resources res = getContext().getResources();
			mFolderUpLayers = new Drawable[] {
					res.getDrawable(R.drawable.ic_launcher_folder_up_closed),
					res.getDrawable(R.drawable.ic_launcher_folder_up_open),
			};
			mFolderLayers = new Drawable[] {
					res.getDrawable(R.drawable.ic_launcher_folder_closed),
					res.getDrawable(R.drawable.ic_launcher_folder_open),
			};
			mFileDrawable = res.getDrawable(R.drawable.file);
		}
		
		public int getCount() {
			return mFolders.size();
		}

		public void add(Folder folder) {
			mFolders.add(folder);
		}

		public void clear() {
			mFolders.clear();
		}

		public Object getItem(int position) {
			return mFolders.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View v = mInflater.inflate(R.layout.folder, parent, false);
			Folder folder = mFolders.get(position);
			TextView name = (TextView) v.findViewById(R.id.folder_name);

			Drawable drawable = null;
			if (folder.isParent) {
				name.setText("[..]");
				drawable = new FolderTransitionDrawable(mFolderUpLayers);
			} else {
				name.setText(folder.getName());
				if (folder.isDirectory()) {
					drawable = new FolderTransitionDrawable(mFolderLayers);
				} else {
					drawable = mFileDrawable;
				}
			}
			v.findViewById(R.id.folder_icon).setBackgroundDrawable(drawable);
			return v;
		}
	}
	
	
	static class FolderTransitionDrawable extends LayerDrawable implements Runnable {
		private static final int TRANSITION_DURATION = 400;
		private static final int[] STATE_SELECTED = {android.R.attr.state_selected};
		private static final int[] STATE_PRESSED = {android.R.attr.state_pressed};
		
		private boolean mActive;
		private int mAlpha;
		private int mFrom;
		private int mTo;
		private long mStartTime;
		private long mEndTime;

		public FolderTransitionDrawable(Drawable[] layers) {
			super(layers);
			mAlpha = 255;
		}
		
		
		@Override
		public boolean isStateful() {
			return true;
		}
		
		@Override
		protected boolean onStateChange(int[] state) {
			boolean active = StateSet.stateSetMatches(STATE_SELECTED, state) | StateSet.stateSetMatches(STATE_PRESSED, state);
			if (active != mActive) {
				mActive = active;
//				Log.d("FolderTransitionDrawable", "onStateChange " + StateSet.dump(state) + " " + active);
				if (!active) {
					unscheduleSelf(this);
					if (mAlpha != 255) {
						startTransition(false);
					}
				} else {
					scheduleSelf(this, SystemClock.uptimeMillis() + 500);
				}
				return true;
			}
			return false;
		}

		public void run() {
			startTransition(true);
		}

		private void startTransition(boolean showLayer1) {
			mStartTime = SystemClock.uptimeMillis();
			mFrom = mAlpha;
			mEndTime = mStartTime;
			if (showLayer1) {
				mTo = 0;
				mEndTime += mAlpha * TRANSITION_DURATION / 255;
			} else {
				mTo = 255;
				mEndTime += (255 - mAlpha) * TRANSITION_DURATION / 255;
			}
			invalidateSelf();
		}

		@Override
		public void draw(Canvas canvas) {
			boolean done = true;
			
			if (mStartTime != 0) {
				long time = SystemClock.uptimeMillis();
				done = time > mEndTime;
				if (done) {
					mStartTime = 0;
					mAlpha = mTo;
				} else {
					float normalized = (time - mStartTime) / (float) (mEndTime - mStartTime);
					mAlpha = (int) (mFrom  + (mTo - mFrom) * normalized);
				}
			}
			
			Drawable d = getDrawable(0);
			d.setAlpha(mAlpha);
			d.draw(canvas);
			
			d = getDrawable(1);
			d.setAlpha(255 - mAlpha);
			d.draw(canvas);
			
			if (!done) {
				invalidateSelf();
//				Log.d("TAG", "draw invalidate");
			}
		}
	}
	
	@SuppressWarnings("serial")
	class Folder extends File {
		private boolean isParent;

		public Folder(File file) {
			super(file.getAbsolutePath());
		}

		public Folder(File file, boolean unused) {
			super(file.getParent());
			isParent = true;
		}
		
		public Folder(String path) {
			super(path);
		}
	}
}
