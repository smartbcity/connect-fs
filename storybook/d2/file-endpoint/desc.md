

# Entrypoints





  
<article>

**fileDelete** ( cmd: [`FileDeleteCommand`](/docs/file-commands--page#filedeletecommand) ) : [`FileDeletedEvent`](/docs/file-commands--page#filedeletedevent) <br/> *Access: fs_write_file* 

Delete a file

</article>
<article>

**fileGet** ( cmd: [`FileGetQuery`](/docs/file-queries--page#filegetquery) ) : [`FileGetResult`](/docs/file-queries--page#filegetresult) <br/> *Access: fs_read_file* 

Fetch a given file descriptor and content

</article>
<article>

**fileList** ( cmd: [`FileListQuery`](/docs/file-queries--page#filelistquery) ) : [`FileListResult`](/docs/file-queries--page#filelistresult) <br/> *Access: fs_read_file* 

Fetch a list of file descriptors

</article>
<article>

**fileUpload** ( cmd: [`FileUploadCommand`](/docs/file-commands--page#fileuploadcommand) ) : [`FileUploadedEvent`](/docs/file-commands--page#fileuploadedevent) <br/> *Access: fs_write_file* 

Upload a file

</article>
<article>

**initPublicDirectory** ( cmd: [`FileInitPublicDirectoryCommand`](/docs/file-commands--page#fileinitpublicdirectorycommand) ) : [`FilePublicDirectoryInitializedEvent`](/docs/file-commands--page#filepublicdirectoryinitializedevent) <br/> *Access: fs_write_policy* 

Grant public access to a given directory

</article>
<article>

**revokePublicDirectory** ( cmd: [`FileRevokePublicDirectoryCommand`](/docs/file-commands--page#filerevokepublicdirectorycommand) ) : [`FilePublicDirectoryRevokedEvent`](/docs/file-commands--page#filepublicdirectoryrevokedevent) <br/> *Access: fs_write_policy* 

Revoke public access to a given directory

</article>

