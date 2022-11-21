Branches:
- standalone-application: This behaves as a standalone Java application, but designed with a specific implementation in mind, will not work for all TestRail domains/runs/suites
- generic-implementation-hook: Requires command-line arguements. Works with all TestRail domains/runs/suites. Can wrap as core program.
- specific-implementation-hook: Requires command-line arguements. Works with only one TestRail domain/run/suite. Can wrap as core program. I use this specific implementation tailored to my current work experience. 


Generic implmentation Brief:
- User selects option, provides run or suite ID. Selenium navigates to run/suite, downloads csv, parses csv and generates a new organized csv in the user's Documents folder. 
