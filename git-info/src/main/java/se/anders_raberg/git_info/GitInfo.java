
package se.anders_raberg.git_info;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

public class GitInfo {

    public static void main(String[] args) throws Exception {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repository = builder.setGitDir(new File("/home/anders/git/MiscExamples/.git")).readEnvironment()
                .findGitDir().build();

        try (Git git = new Git(repository)) {
            String headCommit = repository.resolve(Constants.HEAD).getName();
            List<String> list = git.tagList().call() //
                    .stream() //
                    .map(Ref::getName) //
                    .map(n -> Arrays.asList(n.split("/")).getLast()) //
                    .toList();

            System.out.println(headCommit + " " + list);
        }
    }

}
