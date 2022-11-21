package script.manager;

import script.config.entities.DirectoryConfig;

import java.util.Comparator;

public class DirectoryConfigComparator implements Comparator<DirectoryConfig> {

    @Override
    public int compare(DirectoryConfig o1, DirectoryConfig o2) {
        return o1.getDirectory().getDirectory().compareTo(o2.getDirectory().getDirectory());
    }

}
