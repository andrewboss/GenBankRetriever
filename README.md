GenBankRetriever
================

Java command line application which allows to retrieve sequence from GenBank by using Accession ID and store it in
Google Docs Spreadsheet

General Manual
================

For any questions contact andrew@smartdeveloping.com


What is GenBankRetriever.jar and where to use?
================

It is custom built application used to retrieve fasta formatted nucleotide sequence file from GenBank by its accession ID. It will place fasta file into Google Drive and will record it into Google Spreadsheet. File name will contain unique auto-incremented numeric ID from the spreadsheet.

Is configuration available?
================

Yes, it can be found in the same directory where the jar file is located “config.properties”

TEMP_FILE_PATH =  absolute path to temp directory

GENBANK_SEARCH_URL=http://www.ncbi.nlm.nih.gov/nuccore/

GENBANK_FASTA_DOWNLOAD_URL=http://www.ncbi.nlm.nih.gov/sviewer/viewer.fcgi?tool=portal&sendto=on&log$=seqview&db=nuccore&dopt=fasta&extrafeat=0&maxplex=1&val=

GOOGLE_API_USER= google account username used to access Google Drive and Google Spreadsheets

GOOGLE_API_PASSWORD= google account password

GOOGLE_SPREADSHEET_FEED_URL=https://spreadsheets.google.com/feeds/spreadsheets/private/full

GOOGLE_MASTER_SPREADSHEET= existing spreadsheet name where to keep sequences

GOOGLE_DRIVE_MRNA_SEQUENCES_FOLDER_URL= url to google drive folder to keep fasta files

GOOGLE_DRIVE_MRNA_SEQUENCES_FOLDER_LOCAL_PATH= local path to mounted google drive

How to run GenBankRetriever?
================

Before running it make sure that spreadsheet is sorted by “AccessionID” column in ascending order, otherwise java application won’t be able correctly name new fasta files.

java -jar GenBankRetriever.jar action=insert AccessionID=”access_id_value” COL_NAME=”COL_VALUE” …

The only required parameter is AccessionID, all other provided paramters will represent columns and its values to be added to the spreadsheet upon successful fasta file search and upload.

COL_NAME - column name from the Google spreadsheet where data will be added
COL_VALUE - data to be added to the Google spreadsheet

Example:
java -jar GenBankRetriever.jar AccessionID="D49400" Description="ATP synthase subunit F, vacuolar" TargetSequenceData="AGGGATGGCGGGGAGGG" TargetCoordinates="NA"

Additional actions
================

To extract protein coding region coordinates by using sequence GenBank Accession ID:
java -jar GenBankRetriever.jar action=updatecds

To find Ensemble Transcript Identifiers based on GenBank Accession ID (will use BioDBNet webservice: http://biodbnet.abcc.ncifcrf.gov):
java -jar GenBankRetriever.jar action=updateest



